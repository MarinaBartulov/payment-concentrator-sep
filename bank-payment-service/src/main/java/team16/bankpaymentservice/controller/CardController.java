package team16.bankpaymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team16.bankpaymentservice.dto.ClientAuthDTO;
import team16.bankpaymentservice.model.Card;
import team16.bankpaymentservice.service.CardServiceImpl;

@RestController
@RequestMapping(value = "/api/card")
public class CardController {

    @Autowired
    private CardServiceImpl cardService;

    @PostMapping(value = "/auth", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateClient(@RequestBody ClientAuthDTO dto) {
        System.out.println(dto.getCardHolderName());
        Card card = new Card();
        card.setAvailableFunds(10000);
        card.setCardHolderName("Hhjhjhiu");
        card.setPAN("1111111111111111");
        card.setReservedFunds(300);
        card.setSecurityCode("111");
        Card newCard = cardService.create(card);
        System.out.println(newCard.getExpirationDate());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
