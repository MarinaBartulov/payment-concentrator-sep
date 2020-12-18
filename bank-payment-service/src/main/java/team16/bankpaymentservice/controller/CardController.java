package team16.bankpaymentservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team16.bankpaymentservice.dto.ClientAuthDTO;

@RestController
@RequestMapping(value = "/api/card")
public class CardController {

    @PostMapping(value = "/auth", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateClient(@RequestBody ClientAuthDTO dto) {
        System.out.println(dto.getCardHolderName());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
