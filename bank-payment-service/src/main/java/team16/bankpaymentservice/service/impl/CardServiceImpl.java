package team16.bankpaymentservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.bankpaymentservice.model.Card;
import team16.bankpaymentservice.repository.CardRepository;
import team16.bankpaymentservice.service.CardService;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public Card findByPan(String pan) {
        List<Card> allCards = this.cardRepository.findAll();
        for(Card c: allCards) {
            if(c.getPAN().equals(pan)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public Card create(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public Card update(Card card) {
        return cardRepository.save(card);
    }


}
