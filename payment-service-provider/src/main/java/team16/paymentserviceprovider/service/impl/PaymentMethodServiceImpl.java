package team16.paymentserviceprovider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team16.paymentserviceprovider.dto.FormDataDTO;
import team16.paymentserviceprovider.dto.FormFieldDTO;
import team16.paymentserviceprovider.dto.PaymentMethodDTO;
import team16.paymentserviceprovider.model.App;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.model.PaymentMethod;
import team16.paymentserviceprovider.repository.PaymentMethodRepository;
import team16.paymentserviceprovider.service.MerchantService;
import team16.paymentserviceprovider.service.PaymentMethodService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private RestTemplate restTemplate;


    @Override
    public PaymentMethod findByName(String name) {
        return this.paymentMethodRepository.findByName(name);
    }

    @Override
    public List<FormDataDTO> getFormsDataForAvailablePaymentMethodsForCurrentMerchant(Authentication currentUser) {
        String email = currentUser.getName();
        Merchant merchant = this.merchantService.findByMerchantEmail(email);
        List<FormDataDTO> formsDataDTO = new ArrayList<>();
        for(PaymentMethod pm : merchant.getApp().getPaymentMethods()){

            try {
                ResponseEntity<List<FormFieldDTO>> response = restTemplate.exchange("https://localhost:8083/" + pm.getName().toLowerCase()  + "-payment-service/api/merchant/formFields", HttpMethod.GET, null, new ParameterizedTypeReference<List<FormFieldDTO>>() {
                });
                formsDataDTO.add(new FormDataDTO(pm.getName(), response.getBody()));
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }
        return formsDataDTO;
    }

    @Override
    public PaymentMethod findByPaymentMethodNameAndApp(String paymentMethodName, Long appId) {
        return this.paymentMethodRepository.findByPaymentMethodNameAndAppId(paymentMethodName, appId);
    }

    @Override
    public List<PaymentMethodDTO> getAllPaymentMethods() {
        List<PaymentMethod> paymentMethods = this.paymentMethodRepository.findAll();
        List<PaymentMethodDTO> paymentMethodDTOs = paymentMethods.stream().map(pm -> new PaymentMethodDTO(pm.getId(), pm.getName())).collect(Collectors.toList());
        return paymentMethodDTOs;
    }
}
