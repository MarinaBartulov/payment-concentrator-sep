package team16.bankpaymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.bankpaymentservice.model.Merchant;
import team16.bankpaymentservice.repository.MerchantRepository;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Override
    public Merchant findOne(Long id) {
        return merchantRepository.getOne(id);
    }

    @Override
    public Merchant findByMerchantId(String id) {
        return merchantRepository.findByMerchantId(id);
    }
}
