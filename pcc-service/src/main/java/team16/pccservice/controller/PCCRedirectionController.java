package team16.pccservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.pccservice.dto.PCCRequestDTO;
import team16.pccservice.model.PaymentRequest;
import team16.pccservice.service.PCCService;

@RestController
@RequestMapping(value = "/api")
public class PCCRedirectionController {

    @Autowired
    private PCCService pccService;

    Logger logger = LoggerFactory.getLogger(PCCRedirectionController.class);

    @PostMapping(value = "/redirect", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findBankAndRedirect(@RequestBody PCCRequestDTO dto) {
        try {
            PaymentRequest paymentRequest = pccService.cratePaymentRequest(dto);
            logger.info("Payment Request successfully completed. Sending redirection URL");

            return new ResponseEntity<>(pccService.makeSuccessResponse(paymentRequest), HttpStatus.CREATED);
        } catch(Exception e) {

            logger.error("Exception while completing Payment Request");
            logger.error(e.getMessage());

            return new ResponseEntity<>(pccService.makeFailureResponse(), HttpStatus.OK);
        }
    }
}
