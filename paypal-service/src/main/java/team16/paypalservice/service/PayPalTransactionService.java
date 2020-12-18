package team16.paypalservice.service;

import team16.paypalservice.model.PayPalTransaction;

public interface PayPalTransactionService {

    PayPalTransaction save(PayPalTransaction transaction);
    PayPalTransaction findByPaymentId(String paymentId);
    PayPalTransaction findById(Long transactionId);
}
