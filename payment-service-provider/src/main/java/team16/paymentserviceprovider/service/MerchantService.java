package team16.paymentserviceprovider.service;

import org.springframework.security.core.Authentication;
import team16.paymentserviceprovider.dto.MerchantInfoDTO;
import team16.paymentserviceprovider.dto.MerchantPCDTO;
import team16.paymentserviceprovider.model.App;
import team16.paymentserviceprovider.model.Merchant;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

public interface MerchantService {

    Merchant findOne(Long id);
    List<Merchant> findAll();
    Merchant findByMerchantId(String id);
    Merchant findByMerchantEmail(String email);
    boolean registerNewMerchant(MerchantPCDTO merchantPCDTO) throws MessagingException, InterruptedException;
    MerchantInfoDTO getMyInfo(Authentication currentUser);
    Merchant save(Merchant merchant);
    String addPaymentMethodForCurrentMerchant(String authorization, String paymentMethodName, Map<String, Object> formValues);
}
