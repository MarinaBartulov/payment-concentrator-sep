package team16.bitcoinservice.service;

import team16.bitcoinservice.model.Merchant;

public interface MerchantService {

    Merchant findByEmail(String email);
}
