package team16.paypalservice.service;

import team16.paypalservice.model.PayPalSubscription;

import java.util.List;

public interface PayPalSubscriptionService {

    PayPalSubscription save(PayPalSubscription subscription);
    PayPalSubscription getOne(Long id);
    void findUnfinishedSubscriptions();
    List<PayPalSubscription> findAllUnfinished();
    PayPalSubscription findBySubscriptionId(Long id);
}
