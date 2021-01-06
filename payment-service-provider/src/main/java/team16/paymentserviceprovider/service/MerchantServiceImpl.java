package team16.paymentserviceprovider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.paymentserviceprovider.exceptions.InvalidDataException;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.repository.MerchantRepository;

import java.util.List;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Override
    public Merchant findOne(Long id) {
        return merchantRepository.findById(id).orElseGet(null);
    }

    @Override
    public List<Merchant> findAll() {
        return merchantRepository.findAll();
    }

    @Override
    public Merchant findByMerchantId(String id) {
        return merchantRepository.findMerchantByMerchantId(id);
    }

    @Override
    public Merchant findByMerchantEmail(String email) {
        return merchantRepository.findMerchantByMerchantEmail(email);
    }
}
