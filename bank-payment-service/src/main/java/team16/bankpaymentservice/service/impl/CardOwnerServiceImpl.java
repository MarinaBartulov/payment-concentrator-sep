package team16.bankpaymentservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.bankpaymentservice.model.CardOwner;
import team16.bankpaymentservice.model.Client;
import team16.bankpaymentservice.model.Merchant;
import team16.bankpaymentservice.repository.CardOwnerRepository;
import team16.bankpaymentservice.service.CardOwnerService;

import java.util.List;

@Service
public class CardOwnerServiceImpl implements CardOwnerService {

    @Autowired
    private CardOwnerRepository cardOwnerRepository;

    @Override
    public CardOwner findOne(Long id) {
        return cardOwnerRepository.getOne(id);
    }

    @Override
    public List<CardOwner> findAll() {
        return this.cardOwnerRepository.findAll();
    }

    @Override
    public Merchant findByMerchantId(String merchantId) {
        return cardOwnerRepository.findByMerchantId(merchantId);
    }

    @Override
    public Merchant findByMerchantEmail(String merchantEmail) {
        return cardOwnerRepository.findCardOwnerByMerchantEmail(merchantEmail);
    }

    @Override
    public Client findClientByCardId(Long id) {
        return cardOwnerRepository.findClientByCardId(id);
    }
}
