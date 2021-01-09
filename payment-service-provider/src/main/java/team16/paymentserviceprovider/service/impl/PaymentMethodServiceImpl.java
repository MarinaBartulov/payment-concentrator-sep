package team16.paymentserviceprovider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.paymentserviceprovider.model.PaymentMethod;
import team16.paymentserviceprovider.repository.PaymentMethodRepository;
import team16.paymentserviceprovider.service.PaymentMethodService;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public PaymentMethod findByName(String name) {
        return this.paymentMethodRepository.findByName(name);
    }
}
