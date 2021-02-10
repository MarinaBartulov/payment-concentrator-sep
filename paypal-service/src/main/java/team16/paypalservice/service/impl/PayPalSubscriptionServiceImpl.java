package team16.paypalservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import team16.paypalservice.enums.SubscriptionStatus;
import team16.paypalservice.model.PayPalSubscription;
import team16.paypalservice.repository.PayPalSubscriptionRepository;
import team16.paypalservice.service.PayPalSubscriptionService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PayPalSubscriptionServiceImpl implements PayPalSubscriptionService {

    @Autowired
    PayPalSubscriptionRepository payPalSubscriptionRepository;

    Logger logger = LoggerFactory.getLogger(PayPalSubscriptionServiceImpl.class);

    @Override
    public PayPalSubscription save(PayPalSubscription subscription) {
        return payPalSubscriptionRepository.save(subscription);
    }

    @Override
    public PayPalSubscription getOne(Long id) {
        return payPalSubscriptionRepository.getOne(id);
    }

    @Override
    public List<PayPalSubscription> findAllUnfinished() {
        return payPalSubscriptionRepository.findAllUnfinished();
    }

    @Override
    public PayPalSubscription findBySubscriptionId(Long id) {
        return payPalSubscriptionRepository.findBySubscriptionId(id);
    }

    @Override
    @Scheduled(cron = "0 0/5 * * * *")
    public void findUnfinishedSubscriptions() {
        logger.info("INITIATED FINDING UNFINISHED SUBSCRIPTIONS");
        List<PayPalSubscription> subscriptionList = this.findAllUnfinished();

        for(PayPalSubscription subscription : subscriptionList)
        {
            if(subscription.getCreatedAt().plusMinutes(5).isBefore(LocalDateTime.now())) {
                logger.info("Subscription ID | " + subscription.getId() + " - status changed | EXPIRED");
                subscription.setStatus(SubscriptionStatus.EXPIRED);
                save(subscription);
            }
        }
    }


}
