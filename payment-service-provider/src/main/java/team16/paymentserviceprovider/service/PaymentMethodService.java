package team16.paymentserviceprovider.service;

import team16.paymentserviceprovider.model.PaymentMethod;

public interface PaymentMethodService {

    PaymentMethod findByName(String name);
}
