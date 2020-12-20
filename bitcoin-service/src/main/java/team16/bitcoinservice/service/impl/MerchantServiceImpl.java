package team16.bitcoinservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.bitcoinservice.model.Merchant;
import team16.bitcoinservice.repository.MerchantRepository;
import team16.bitcoinservice.service.MerchantService;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Override
    public Merchant findByEmail(String email) {
        return this.merchantRepository.findByEmail(email);
    }
}
