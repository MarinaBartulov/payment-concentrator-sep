package team16.bankpaymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import team16.bankpaymentservice.dto.MerchantDTO;
import team16.bankpaymentservice.dto.TransactionDTO;
import team16.bankpaymentservice.model.Merchant;
import team16.bankpaymentservice.service.MerchantService;

import java.net.URISyntaxException;

@RestController
@RequestMapping(value="/api/merchant")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = "/formFields")
    public ResponseEntity getFormFields(){

        return new ResponseEntity(this.merchantService.getFormFields(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addNewMerchant(@RequestHeader("Authorization") String authToken, @RequestBody String merchantData){
        System.out.println(authToken);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
        String email = "";

        try {
            ResponseEntity<String> response = restTemplate.exchange("https://localhost:8083/psp-service/api/merchant/current", HttpMethod.GET, httpEntity, String.class);
            email = response.getBody();
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error occurred while authenticating merchant.");
        }

        Merchant merchant = this.merchantService.findByEmail(email);
        if(merchant != null){
            return ResponseEntity.badRequest().body("This merchant has already chosen bank payment method.");
        }

        Merchant saved = null;
        try {
            saved = merchantService.addNewMerchant(merchantData, email);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        // vratiti informacije o merchant id i merchant password koji su se sad generisali na psp
        System.out.println("New merchant: " + saved.getId());
        // zahtev se salje na PSP
        MerchantDTO merchantDTO = new MerchantDTO(saved.getMerchantEmail(), saved.getMerchantId(), saved.getPassword());
        HttpEntity<MerchantDTO> request = new HttpEntity<>(merchantDTO, headers);

        try {
            ResponseEntity<MerchantDTO> response = restTemplate.exchange("https://localhost:8083/psp-service/api/merchant/save-info-from-bank", HttpMethod.POST, request, MerchantDTO.class);
            if(response.getBody() == null) {
                return ResponseEntity.badRequest().body("Error occurred while saving merchant on Payment Concentrator.");
            }
            System.out.println("Response: " + response.getBody().getMerchantId() + " | " + response.getBody().getMerchantPassword());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error occurred while saving merchant on Payment Concentrator.");
        }

        return ResponseEntity.ok().build();

    }
}
