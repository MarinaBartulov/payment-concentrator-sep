package team16.paymentserviceprovider.service;

import team16.paymentserviceprovider.model.Merchant;

public interface MerchantService {

    Merchant findOne(Long id);
    Merchant findByMerchantId(String id);

}
