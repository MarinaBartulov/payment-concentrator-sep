package team16.bankpaymentservice.controller;

import com.google.gson.Gson;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import team16.bankpaymentservice.dto.MerchantCardInfoDTO;
import team16.bankpaymentservice.dto.PanDTO;
import team16.bankpaymentservice.model.Bank;
import team16.bankpaymentservice.model.Card;
import team16.bankpaymentservice.model.Merchant;
import team16.bankpaymentservice.service.BankService;
import team16.bankpaymentservice.service.CardService;
import team16.bankpaymentservice.service.MerchantService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/api/merchant")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CardService cardService;

    @Autowired
    private BankService bankService;

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
            // treba da dobavim i jos jednu merchant info - name? proveriti da li mi treba
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error occurred while authenticating merchant.");
        }

        Merchant merchant = this.merchantService.findByEmail(email);
        if(merchant != null){
            return ResponseEntity.badRequest().body("This merchant has already chosen bank payment method.");
        }

//        Merchant newMerchant = this.merchantService.addNewMerchant(merchantData, email);
//
//        if(newMerchant == null){
//            return ResponseEntity.badRequest().body("Invalid merchant data.");
//        }

        // kao merchant data stize pan
        Gson gson = new Gson();
        PanDTO pan = null;
        try {
           pan = gson.fromJson(merchantData, PanDTO.class);
        } catch(Exception e){
            return ResponseEntity.badRequest().body("Invalid PAN.");
        }

        Card card = cardService.findByPan(pan.getPan());
        if(card == null) {
            return ResponseEntity.badRequest().body("Card with this PAN doesn't exist.");
        }

        Bank bank = bankService.findByBankCode(card.getPAN().substring(0, 3));
        if(bank == null) {
            return ResponseEntity.badRequest().body("Bank with this code doesn't exist.");
        }

        Merchant newMerchant = new Merchant();
        newMerchant.setMerchantEmail(email);
        newMerchant.setCard(card);
        newMerchant.setBank(bank);
        newMerchant.setMerchantId(generateCommonLangPassword(6)); // 30 karaktera
        newMerchant.setPassword(generateCommonLangPassword(2)); // 10 karaktera

        try {
            merchantService.save(newMerchant);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Saving merchant failed.");
        }

        // vratiti informacije o merchant id i merchant password koji su se sad generisali na psp

        return ResponseEntity.ok().build();

    }

    private String generateCommonLangPassword(int count) {
        String upperCaseLetters = RandomStringUtils.random(count, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(count, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(count);
        String specialChar = RandomStringUtils.random(count, 33, 47, false, false);
        String totalChars = RandomStringUtils.randomAlphanumeric(count);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
                .concat(numbers)
                .concat(specialChar)
                .concat(totalChars);
        List<Character> pwdChars = combinedChars.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        String password = pwdChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        return password;
    }


    @PostMapping(value = "/card-auth/{merchantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateClient(@PathVariable Long merchantId, @RequestBody MerchantCardInfoDTO dto) {
        try {
            Card card = merchantService.merchantCardAuth(dto, merchantId);
            return new ResponseEntity<>(card, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
