package team16.bankpaymentservice.controller;

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
import team16.bankpaymentservice.dto.PCCRequestDTO;
import team16.bankpaymentservice.service.IssuerService;

@RestController
@RequestMapping(value = "/api/issuer")
public class IssuerController {

    @Autowired
    private IssuerService issuerService;

    Logger logger = LoggerFactory.getLogger(IssuerController.class);

    @PostMapping(value = "/pcc-payment-request", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> handlePCCPaymentRequest(@RequestBody PCCRequestDTO dto) {
        String response;
        try {
            response = issuerService.handlePCCPaymentRequest(dto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception e) {
            response = "auc";
            logger.error("ERROR | while completing transaction: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
