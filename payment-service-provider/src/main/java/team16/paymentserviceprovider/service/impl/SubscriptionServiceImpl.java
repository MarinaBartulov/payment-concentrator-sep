package team16.paymentserviceprovider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.paymentserviceprovider.model.Subscription;
import team16.paymentserviceprovider.repository.SubscriptionRepository;
import team16.paymentserviceprovider.service.SubscriptionService;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription save(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription getOne(Long subscriptionId) {
        return subscriptionRepository.getOne(subscriptionId);
    }
}
