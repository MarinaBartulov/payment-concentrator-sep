package team16.paymentserviceprovider.service;

import team16.paymentserviceprovider.dto.MerchantPCDTO;
import team16.paymentserviceprovider.model.App;
import team16.paymentserviceprovider.model.Merchant;

import javax.mail.MessagingException;
import java.util.List;

public interface MerchantService {

    Merchant findOne(Long id);
    List<Merchant> findAll();
    Merchant findByMerchantId(String id);
    Merchant findByMerchantEmail(String email);
    boolean registerNewMerchant(MerchantPCDTO merchantPCDTO) throws MessagingException, InterruptedException;
}
