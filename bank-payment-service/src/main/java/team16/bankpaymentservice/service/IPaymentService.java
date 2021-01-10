package team16.bankpaymentservice.service;

import team16.bankpaymentservice.model.Payment;

public interface IPaymentService {

    Payment findById(Long id);

    Payment update(Payment payment);
}
