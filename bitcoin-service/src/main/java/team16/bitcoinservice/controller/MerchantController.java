package team16.bitcoinservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import team16.bitcoinservice.model.Merchant;
import team16.bitcoinservice.service.MerchantService;

@RestController
@RequestMapping(value="/api/merchant")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(MerchantController.class);



    @GetMapping(value = "/formFields")
    public ResponseEntity getFormFields(){

          return new ResponseEntity(this.merchantService.getFormFields(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addNewMerchant(@RequestHeader("Authorization") String authToken, @RequestBody String merchantData){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
        String email = "";

        try {
            ResponseEntity<String> response = restTemplate.exchange("https://localhost:8083/psp-service/api/merchant/current", HttpMethod.GET, httpEntity, String.class);
            email = response.getBody();
        }catch(Exception e){
            logger.error("Error occurred while authenticating merchant.");
            return ResponseEntity.badRequest().body("Error occurred while authenticating merchant.");
        }

        Merchant merchant = this.merchantService.findByEmail(email);
        if(merchant != null){
            logger.warn("Merchant with email " + merchant.getEmail() + " has already chosen bitcoin payment method.");
            return ResponseEntity.badRequest().body("This merchant has already chosen bitcoin payment method.");
        }

        Merchant newMerchant = this.merchantService.addNewMerchant(merchantData, email);

        if(newMerchant == null){
            return ResponseEntity.badRequest().body("Invalid merchant data.");
        }

        return ResponseEntity.ok().build();

    }
}
