package team16.bankpaymentservice.service;

import team16.bankpaymentservice.model.CardOwner;
import team16.bankpaymentservice.model.Client;
import team16.bankpaymentservice.model.Merchant;

public interface CardOwnerService {

    CardOwner findOne(Long id);
    Merchant findByMerchantId(String merchantId);
    Merchant findByMerchantEmail(String merchantEmail);
    Client findClientByCardId(Long id);
}
