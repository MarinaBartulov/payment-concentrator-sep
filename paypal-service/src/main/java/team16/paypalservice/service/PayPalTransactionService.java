package team16.paypalservice.service;

import team16.paypalservice.model.PayPalTransaction;

import java.util.List;

public interface PayPalTransactionService {

    PayPalTransaction save(PayPalTransaction transaction);
    PayPalTransaction findByPaymentId(String paymentId);
    PayPalTransaction findById(Long transactionId);
    void findUnfinishedTransactions();
    List<PayPalTransaction> findAllUnfinished();
    PayPalTransaction findTransactionByOrderId(Long orderId);
}
