package team16.bankpaymentservice.service;

import team16.bankpaymentservice.model.Card;

public interface CardService {

    Card findByPan(String pan);

    Card create(Card card);

    Card update(Card card);
}
