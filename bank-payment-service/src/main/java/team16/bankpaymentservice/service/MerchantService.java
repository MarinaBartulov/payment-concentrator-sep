package team16.bankpaymentservice.service;

import team16.bankpaymentservice.model.Merchant;

public interface MerchantService {

    Merchant findOne(Long id);
    Merchant findByMerchantId(String id);
}
