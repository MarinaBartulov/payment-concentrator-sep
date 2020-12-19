package team16.bitcoinservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import team16.bitcoinservice.dto.BitcoinPaymentDTO;
import team16.bitcoinservice.dto.PaymentRequestDTO;
import team16.bitcoinservice.dto.PaymentResponseDTO;
import team16.bitcoinservice.model.Merchant;
import team16.bitcoinservice.model.Transaction;
import team16.bitcoinservice.service.MerchantService;
import team16.bitcoinservice.service.TransactionService;

@RestController
@RequestMapping("/api")
public class BitcoinPaymentController {

    @Value("${success_url}")
    private String successUrl;

    @Value("${cancel_url}")
    private String cancelUrl;

    @Value("${callback_url}")
    private String callbackUrl;

    @Value("${sandbox_url}")
    private String sandboxUrl;


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private TransactionService transactionService;


    //@PostMapping("/create")
    @PostMapping("/pay")
    public ResponseEntity createPayment(@RequestBody BitcoinPaymentDTO bitcoinPaymentDTO){


        Merchant merchant = this.merchantService.findByEmail(bitcoinPaymentDTO.getMerchantEmail());
        if(merchant == null){
            return ResponseEntity.badRequest().body("Merchant with that email does not exist.");
        }

        Transaction transaction = this.transactionService.createTransaction(merchant,bitcoinPaymentDTO);

        if(transaction == null){
            return ResponseEntity.badRequest().body("Saving new transaction failed.");
        }


        PaymentRequestDTO requestDTO = new PaymentRequestDTO(bitcoinPaymentDTO.getOrderId().toString(),bitcoinPaymentDTO.getAmount(),
                bitcoinPaymentDTO.getCurrency(),"BTC",this.callbackUrl,
                this.cancelUrl + "/" + transaction.getId(),
                this.successUrl + "/" + transaction.getId(),merchant.getToken());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + " " + merchant.getToken());
        HttpEntity<PaymentRequestDTO> request = new HttpEntity<>(requestDTO, headers);

        ResponseEntity<PaymentResponseDTO> responseEntity = null;

        try {
            responseEntity = restTemplate.exchange(this.sandboxUrl, HttpMethod.POST, request, PaymentResponseDTO.class);
        }catch(Exception e){
             this.transactionService.changeTransactionStatus(transaction.getId(),"INVALID");
             return ResponseEntity.badRequest().build();
        }

        PaymentResponseDTO response = responseEntity.getBody();
        transaction = this.transactionService.updateTransaction(transaction, response);

        if(transaction == null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response.getPayment_url());
    }

    @GetMapping("/success")
    public ResponseEntity success(@RequestParam Long id){

        System.out.println("Uslo u success");
        Transaction transaction = this.transactionService.findTransactionById(id);
        if(transaction == null){
            return ResponseEntity.notFound().build();
        }

        if(this.transactionService.updateTransactionFromCoinGate(transaction)){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/cancel")
    public ResponseEntity cancel(@RequestParam Long id){

        System.out.println("Uslo u cancel: " + id);
        Transaction transaction = this.transactionService.findTransactionById(id);
        if(transaction == null){
            return ResponseEntity.notFound().build();
        }

        if(this.transactionService.updateTransactionFromCoinGate(transaction)){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.badRequest().build();
        }
    }


}
