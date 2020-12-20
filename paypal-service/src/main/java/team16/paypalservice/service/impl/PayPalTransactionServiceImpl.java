package team16.paypalservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.paypalservice.model.PayPalTransaction;
import team16.paypalservice.repository.PayPalTransactionRepository;
import team16.paypalservice.service.PayPalTransactionService;

import java.util.Optional;

@Service
public class PayPalTransactionServiceImpl implements PayPalTransactionService {

    @Autowired
    PayPalTransactionRepository transactionRepository;

    @Override
    public PayPalTransaction save(PayPalTransaction transaction) {

        return transactionRepository.save(transaction);
    }

    @Override
    public PayPalTransaction findByPaymentId(String paymentId) {

        return transactionRepository.findByPaymentId(paymentId);
    }

    @Override
    public PayPalTransaction findById(Long transactionId) {

        return transactionRepository.findById(transactionId).orElse(null);
    }


}
