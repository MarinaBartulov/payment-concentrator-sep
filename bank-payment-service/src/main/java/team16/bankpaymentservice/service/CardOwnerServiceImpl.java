package team16.bankpaymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.bankpaymentservice.model.CardOwner;
import team16.bankpaymentservice.model.Client;
import team16.bankpaymentservice.model.Merchant;
import team16.bankpaymentservice.repository.CardOwnerRepository;

@Service
public class CardOwnerServiceImpl implements CardOwnerService {

    @Autowired
    private CardOwnerRepository cardOwnerRepository;

    @Override
    public CardOwner findOne(Long id) {
        return cardOwnerRepository.getOne(id);
    }

    @Override
    public Merchant findByMerchantId(String merchantId) {
        return cardOwnerRepository.findByMerchantId(merchantId);
    }

    @Override
    public Client findClientByCardId(Long id) {
        return cardOwnerRepository.findClientByCardId(id);
    }
}
