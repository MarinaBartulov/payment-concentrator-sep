package team16.bitcoinservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import team16.bitcoinservice.dto.BitcoinPaymentDTO;
import team16.bitcoinservice.dto.OrderStatusDTO;
import team16.bitcoinservice.dto.PaymentRequestDTO;
import team16.bitcoinservice.dto.PaymentResponseDTO;
import team16.bitcoinservice.enums.TransactionStatus;
import team16.bitcoinservice.model.Merchant;
import team16.bitcoinservice.model.Transaction;
import team16.bitcoinservice.service.MerchantService;
import team16.bitcoinservice.service.TransactionService;

import javax.validation.Valid;
import java.text.MessageFormat;

@RestController
@RequestMapping("/api")
public class BitcoinController {

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

    Logger logger = LoggerFactory.getLogger(BitcoinController.class);


    @PostMapping("/pay")
    public ResponseEntity createPayment(@RequestBody @Valid BitcoinPaymentDTO bitcoinPaymentDTO){

        logger.info(MessageFormat.format("Creating payment | OrderId: {0} | Amount: {1} {2} | Merchant email: {3}",
                bitcoinPaymentDTO.getOrderId(), bitcoinPaymentDTO.getAmount(),bitcoinPaymentDTO.getCurrency(),
                bitcoinPaymentDTO.getMerchantEmail()));

        Merchant merchant = this.merchantService.findByEmail(bitcoinPaymentDTO.getMerchantEmail());
        if(merchant == null){

            logger.warn("Merchant not found | Email: " + bitcoinPaymentDTO.getMerchantEmail());
            return ResponseEntity.badRequest().body("Merchant with that email does not exist.");
        }

        Transaction transaction = this.transactionService.createTransaction(merchant,bitcoinPaymentDTO);

        if(transaction == null){

            logger.error(MessageFormat.format("Saving new transaction for the order failed | OrderId: {0} | Merchant's email: {1}",
                    bitcoinPaymentDTO.getOrderId(), bitcoinPaymentDTO.getMerchantEmail()));
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
             logger.error("Communication with CoinGate api failed | TransactionId: " + transaction.getId());
             this.transactionService.changeTransactionStatus(transaction.getId(),"INVALID");
             return ResponseEntity.badRequest().build();
        }

        PaymentResponseDTO response = responseEntity.getBody();
        transaction = this.transactionService.updateTransaction(transaction, response);

        if(transaction == null){
            logger.error(MessageFormat.format("Updating transaction for the payment failed | TransactionId: {0} | PaymentId: {1}",
                    transaction.getId(), transaction.getPaymentId()));
            return ResponseEntity.badRequest().build();
        }

        logger.info(MessageFormat.format("Creating payment completed | OrderId: {0} | Amount: {1} {2} | Merchant email: {3}",
                bitcoinPaymentDTO.getOrderId(),bitcoinPaymentDTO.getAmount(), bitcoinPaymentDTO.getCurrency(), bitcoinPaymentDTO.getMerchantEmail()));

        return ResponseEntity.ok(response.getPayment_url());
    }

    @GetMapping("/success")
    public ResponseEntity success(@RequestParam Long id){

        logger.info("SUCCESS endpoint | Changing transaction status | TransactionId: " + id);

        Transaction transaction = this.transactionService.findTransactionById(id);
        if(transaction == null){

            logger.warn("Transaction not found | TransactionId: " + id);
            return ResponseEntity.badRequest().body(transaction.getErrorUrl());
        }

        if(this.transactionService.updateTransactionFromCoinGate(transaction)){
            return ResponseEntity.ok().body(transaction.getSuccessUrl());
        }else{
            return ResponseEntity.badRequest().body(transaction.getErrorUrl());
        }

    }

    @GetMapping("/cancel")
    public ResponseEntity cancel(@RequestParam Long id){

        logger.info("CANCEL endpoint | Changing transaction status | TransactionId: " + id);

        Transaction transaction = this.transactionService.findTransactionById(id);
        if(transaction == null){

            logger.warn("Transaction not found | TransactionId: " + id);
            return ResponseEntity.badRequest().body(transaction.getErrorUrl());
        }

        if(this.transactionService.updateTransactionFromCoinGate(transaction)){
            return ResponseEntity.ok().body(transaction.getFailedUrl());
        }else{
            return ResponseEntity.badRequest().body(transaction.getErrorUrl());
        }
    }

    @GetMapping("/status")
    public ResponseEntity checkTransactionStatus(@RequestParam("orderId") Long orderId){

         Transaction tr = this.transactionService.findTransactionByOrderId(orderId);
         if(tr == null){
             return ResponseEntity.badRequest().body("Transaction with orderId " + orderId + " doesn't exist.");
         }

        OrderStatusDTO orderStatusDTO = new OrderStatusDTO();
          if(tr.getStatus() == TransactionStatus.NEW || tr.getStatus() == TransactionStatus.PENDING
              || tr.getStatus() == TransactionStatus.CONFIRMING){
              orderStatusDTO.setStatus("CREATED");
          }else if(tr.getStatus() == TransactionStatus.PAID){
              orderStatusDTO.setStatus("COMPLETED");
          }else if(tr.getStatus() == TransactionStatus.CANCELED || tr.getStatus() == TransactionStatus.REFUNDED){
              orderStatusDTO.setStatus("CANCELED");
          }else{
              orderStatusDTO.setStatus(tr.getStatus().toString());
          }
          return new ResponseEntity(orderStatusDTO, HttpStatus.OK);
    }


}
