package team16.paypalservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.paypalservice.model.PayPalSubscription;
import team16.paypalservice.repository.PayPalSubscriptionRepository;
import team16.paypalservice.service.PayPalSubscriptionService;

@Service
public class PayPalSubscriptionServiceImpl implements PayPalSubscriptionService {

    @Autowired
    PayPalSubscriptionRepository payPalSubscriptionRepository;


    @Override
    public PayPalSubscription save(PayPalSubscription subscription) {
        return payPalSubscriptionRepository.save(subscription);
    }

    @Override
    public PayPalSubscription getOne(Long id) {
        return payPalSubscriptionRepository.getOne(id);
    }
}
