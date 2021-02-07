package team16.bankpaymentservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.bankpaymentservice.dto.OrderStatusDTO;
import team16.bankpaymentservice.dto.PaymentRequestDTO;
import team16.bankpaymentservice.dto.PaymentResponseInfoDTO;
import team16.bankpaymentservice.enums.TransactionStatus;
import team16.bankpaymentservice.model.Transaction;
import team16.bankpaymentservice.service.PaymentService;
import team16.bankpaymentservice.service.TransactionService;

@RestController
@RequestMapping(value = "/api")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TransactionService transactionService;

    Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @PostMapping(value = "/payments/request")
    public ResponseEntity<?> createPaymentRequest(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        try {
            PaymentResponseInfoDTO paymentResponseInfoDTO = paymentService.generatePaymentInfo(paymentRequestDTO);
            logger.info("Payment successfully created. Sending payment URL");
            return new ResponseEntity<>(paymentResponseInfoDTO, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception while creating payment");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/status")
    public ResponseEntity checkTransactionStatus(@RequestParam("orderId") Long orderId){
        // orderId je na Banci merchantOrderId
        Transaction tr = this.transactionService.findByMerchantOrderId(orderId);
        if(tr == null) {
            logger.error("Transaction with given Merchant Order Id not found.");
            return ResponseEntity.badRequest().body("Transaction with given Merchant Order Id not found.");
        }

        OrderStatusDTO orderStatusDTO = new OrderStatusDTO();
        if(tr.getStatus() == TransactionStatus.INITIATED || tr.getStatus() == TransactionStatus.CREATED){
            orderStatusDTO.setStatus("CREATED");
        }else if(tr.getStatus() == TransactionStatus.COMPLETED){
            orderStatusDTO.setStatus("COMPLETED");
        }else if(tr.getStatus() == TransactionStatus.NONEXISTENT || tr.getStatus() == TransactionStatus.FAILED){
            orderStatusDTO.setStatus("INVALID");
        }

        logger.info("Transaction status successfully sent.");

        return new ResponseEntity(orderStatusDTO, HttpStatus.OK);
    }

}
