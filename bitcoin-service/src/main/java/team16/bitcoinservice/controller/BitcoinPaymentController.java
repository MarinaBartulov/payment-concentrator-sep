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
@RequestMapping("/")
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


    @PostMapping("/create")
    public ResponseEntity createPayment(@RequestBody BitcoinPaymentDTO bitcoinPaymentDTO){


        Merchant merchant = this.merchantService.findByEmail(bitcoinPaymentDTO.getEmail());

        Transaction transaction = this.transactionService.createTransaction(merchant,bitcoinPaymentDTO);

        PaymentRequestDTO requestDTO = new PaymentRequestDTO(bitcoinPaymentDTO.getOrderId().toString(),bitcoinPaymentDTO.getPaymentAmount(),
                bitcoinPaymentDTO.getPaymentCurrency(),"BTC",this.callbackUrl,
                this.cancelUrl + "/" + transaction.getId(),
                this.successUrl + "/" + transaction.getId(),merchant.getToken());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + " " + merchant.getToken());
        HttpEntity<PaymentRequestDTO> request = new HttpEntity<>(requestDTO, headers);

        ResponseEntity<PaymentResponseDTO> responseEntity = null;
        responseEntity = restTemplate.exchange(this.sandboxUrl, HttpMethod.POST, request, PaymentResponseDTO.class);

        PaymentResponseDTO response = responseEntity.getBody();


        return ResponseEntity.ok(response.getPayment_url());
    }

    @GetMapping("/success")
    public ResponseEntity success(@RequestParam Long id){

        System.out.println("Uslo u success");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cancel")
    public ResponseEntity cancel(@RequestParam Long id){

        System.out.println("Uslo u cancel: " + id);

        return ResponseEntity.ok().build();
    }


}
