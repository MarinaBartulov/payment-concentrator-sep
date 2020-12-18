package team16.bankpaymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.bankpaymentservice.model.Card;
import team16.bankpaymentservice.repository.CardRepository;

import java.time.YearMonth;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public Card create(Card card) {
        card.setExpirationDate(YearMonth.of(2025, 4));
        return cardRepository.save(card);
    }

    // validirati DTO
    // dodati Banku kao entitet i njenu sifru, pa porveriti na osnovu njene sifre
    // proveriti da li je to pan iz te banke
    // ako da
    // proveriti da li se svi podaci poklapaju sa onima u bazi
    // ako ne
    // onda ide na pcc
}
