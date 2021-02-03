package team16.paymentserviceprovider.service;

import team16.paymentserviceprovider.dto.SubscriptionRequestDTO;
import team16.paymentserviceprovider.dto.SubscriptionResponseDTO;
import team16.paymentserviceprovider.model.BillingPlan;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.model.Subscription;

public interface SubscriptionService {

    Subscription save(Subscription subscription);
    Subscription getOne(Long subscriptionId);
    SubscriptionResponseDTO createSubscriptionFromLA(SubscriptionRequestDTO dto, Merchant merchant, BillingPlan billingPlan);
    String createSubscription(Subscription subscription);
}
