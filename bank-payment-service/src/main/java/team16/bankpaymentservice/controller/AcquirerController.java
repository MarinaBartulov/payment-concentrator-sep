package team16.bankpaymentservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.bankpaymentservice.dto.ClientAuthDTO;
import team16.bankpaymentservice.dto.TransactionResponseDTO;
import team16.bankpaymentservice.dto.PCCResponseDTO;
import team16.bankpaymentservice.service.AcquirerService;

@RestController
@RequestMapping(value = "/api/acquirer")
public class AcquirerController {

    @Autowired
    private AcquirerService acquirerService;

    Logger logger = LoggerFactory.getLogger(AcquirerController.class);

    @PostMapping(value = "/initialize-payment/{paymentId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateClient(@PathVariable Long paymentId, @RequestBody ClientAuthDTO dto) {
        TransactionResponseDTO response = new TransactionResponseDTO();

        try {
            response = acquirerService.initialPayment(dto, paymentId);

            if(response.getTransactionStatus().equals("COMPLETED")) {
                logger.info("Transaction successfully completed. Sending redirection URL");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            logger.info("Transaction IS NOT completed. Sending redirection URL");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception e) {
            response.setResponseMessage(e.getMessage());
            logger.error("ERROR | while completing transaction: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
