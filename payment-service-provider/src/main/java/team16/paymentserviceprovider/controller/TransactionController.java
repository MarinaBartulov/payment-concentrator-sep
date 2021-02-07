package team16.paymentserviceprovider.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team16.paymentserviceprovider.dto.TransactionDTO;
import team16.paymentserviceprovider.model.BankTransaction;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.model.Order;
import team16.paymentserviceprovider.service.TransactionService;

@RestController
@RequestMapping(value = "/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @PostMapping(value = "/bank", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO dto) {
        String response = "";
        try {
            BankTransaction transaction = transactionService.create(dto);
            response = "Transaction created successfully with status: " + transaction.getStatus();
            logger.info("Transaction successfully created on PSP service.");

            Merchant merchant = transaction.getOrder().getMerchant();
            if(transaction.getStatus().equals("NONEXISTENT") || transaction.getStatus().equals("FAILED")) {
                response = merchant.getMerchantFailedUrl();
            } else {
                response = merchant.getMerchantSuccessUrl();
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception while creating transaction");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
