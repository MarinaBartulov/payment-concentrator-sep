package team16.paymentserviceprovider.service;

import team16.paymentserviceprovider.model.Subscription;

public interface SubscriptionService {

    Subscription save(Subscription subscription);
    Subscription getOne(Long subscriptionId);
}
