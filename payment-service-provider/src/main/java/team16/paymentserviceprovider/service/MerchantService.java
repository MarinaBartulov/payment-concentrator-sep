package team16.paymentserviceprovider.service;

import team16.paymentserviceprovider.model.Merchant;

import java.util.List;

public interface MerchantService {

    Merchant findOne(Long id);
    List<Merchant> findAll();
    Merchant findByMerchantId(String id);
    Merchant findByMerchantEmail(String email);
}
