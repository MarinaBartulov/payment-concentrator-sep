package team16.paypalservice.service;

import team16.paypalservice.model.PayPalSubscription;

public interface PayPalSubscriptionService {

    PayPalSubscription save(PayPalSubscription subscription);
    PayPalSubscription getOne(Long id);
}
