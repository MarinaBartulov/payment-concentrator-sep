package team16.paymentserviceprovider.service;

import org.springframework.security.core.Authentication;
import team16.paymentserviceprovider.dto.FormDataDTO;
import team16.paymentserviceprovider.dto.PaymentMethodDTO;
import team16.paymentserviceprovider.model.App;
import team16.paymentserviceprovider.model.PaymentMethod;

import java.util.List;

public interface PaymentMethodService {

    PaymentMethod findByName(String name);
    List<FormDataDTO> getFormsDataForAvailablePaymentMethodsForCurrentMerchant(Authentication currentUser);
    PaymentMethod findByPaymentMethodNameAndApp(String paymentMethodName, Long appId);
    List<PaymentMethodDTO> getAllPaymentMethods();
}
