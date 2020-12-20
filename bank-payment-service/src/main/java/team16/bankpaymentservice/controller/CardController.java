package team16.bankpaymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.bankpaymentservice.dto.ClientAuthDTO;
import team16.bankpaymentservice.dto.OnlyAcquirerTransactionResponseDTO;
import team16.bankpaymentservice.service.CardServiceImpl;

@RestController
@RequestMapping(value = "/api/card")
public class CardController {

    @Autowired
    private CardServiceImpl cardService;

    @PostMapping(value = "/client-auth/{paymentId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateClient(@PathVariable Long paymentId, @RequestBody ClientAuthDTO dto) {
        OnlyAcquirerTransactionResponseDTO response = new OnlyAcquirerTransactionResponseDTO();
        try {
            response = cardService.handleClientAuthentication(dto, paymentId);
            if(!response.getTransactionStatus().equals("COMPLETED")) {
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception e) {
            response.setResponseMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
