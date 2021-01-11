package team16.bankpaymentservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.bankpaymentservice.model.Payment;
import team16.bankpaymentservice.repository.PaymentRepository;
import team16.bankpaymentservice.service.IPaymentService;

@Service
public class PaymentServiceImpl implements IPaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment findById(Long id) {
        return paymentRepository.getOne(id);
    }

    @Override
    public Payment findByTransactionId(Long transactionId) {
        return paymentRepository.findPaymentByTransactionId(transactionId);
    }

    @Override
    public Payment update(Payment payment) {
        return paymentRepository.save(payment);
    }
}
