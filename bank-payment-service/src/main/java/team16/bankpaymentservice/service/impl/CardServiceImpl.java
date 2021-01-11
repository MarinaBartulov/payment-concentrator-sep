package team16.bankpaymentservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.bankpaymentservice.model.Card;
import team16.bankpaymentservice.repository.CardRepository;
import team16.bankpaymentservice.service.CardService;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public Card findByPan(String pan) {
        return cardRepository.findByPan(pan);
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
