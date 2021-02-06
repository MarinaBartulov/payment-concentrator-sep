package team16.paymentserviceprovider.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import team16.paymentserviceprovider.config.EndpointConfig;
import team16.paymentserviceprovider.config.RestConfig;
import team16.paymentserviceprovider.dto.*;
import team16.paymentserviceprovider.enums.SubscriptionStatus;
import team16.paymentserviceprovider.model.*;
import team16.paymentserviceprovider.repository.SubscriptionRepository;
import team16.paymentserviceprovider.service.PaymentMethodService;
import team16.paymentserviceprovider.service.SubscriptionService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private BillingPlanServiceImpl billingPlanService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestConfig configuration;

    Logger logger = LoggerFactory.getLogger(SubscriptionServiceImpl.class);

    @Override
    public Subscription save(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription getOne(Long subscriptionId) {
        return subscriptionRepository.getOne(subscriptionId);
    }

    @Override
    public SubscriptionResponseDTO createSubscriptionFromLA(SubscriptionRequestDTO dto, Merchant merchant, BillingPlan billingPlan) {

        Subscription subscription = new Subscription(dto, merchant, billingPlan);
        Subscription savedSubscription = save(subscription);
        logger.info("Saved subscription | ID " + savedSubscription.getId());
        String redirectionURL = "https://localhost:3001/subscription/id/" + savedSubscription.getId();

        SubscriptionResponseDTO subscriptionResponseDTO = new SubscriptionResponseDTO(subscription, billingPlan, merchant, redirectionURL);

        return subscriptionResponseDTO;
    }

    @Override
    public String createSubscription(Subscription subscription)  {
        Merchant merchant = subscription.getMerchant();
        if(merchant == null){
            logger.error("Failed to find Merchant | ID: " + subscription.getMerchant().getMerchantId());
            return null;
        }
        logger.info("Found Merchant | ID: " + merchant.getId());

        BillingPlan billingPlan = billingPlanService.getOne(subscription.getBillingPlan().getId());
        if(billingPlan == null)
        {
            logger.error("Failed to find Billing plan | ID: " + subscription.getBillingPlan().getId());
            return null;
        }
        logger.info("Found Billing plan ID | " + billingPlan.getId());

        PaymentMethod pm = this.paymentMethodService.findByName("PayPal");
        subscription.setPaymentMethod(pm);
        subscription.setStatus(SubscriptionStatus.CREATED);
        save(subscription);

        SubscriptionInfoDTO subscriptionInfoDTO = new SubscriptionInfoDTO(subscription, merchant, billingPlan);

        HttpEntity<SubscriptionInfoDTO> request = new HttpEntity<>(subscriptionInfoDTO);
        ResponseEntity<String> response = null;

        try {
            logger.info("Sending request to paypal payment service");
            response = restTemplate.exchange( configuration.url() + EndpointConfig.PAYPAL_PAYMENT_SERVICE_BASE_URL + "/api/subscription/create"
                    , HttpMethod.POST, request, String.class);
            logger.info("Received response from paypal payment service");
        } catch (RestClientException e) {
            logger.error("RestTemplate error");
            e.printStackTrace();
        }

        return response.getBody();
    }

    @Override
    public Subscription findById(Long id) {
        return subscriptionRepository.findById(id).orElse(null);
    }

    @Scheduled(initialDelay = 60000, fixedRate = 300000) //delay je 1 min, posle na svakih 5 minuta
    public void updateSubscriptionStatus(){

        logger.info("Updating subscription status started...");
        List<Subscription> expiredSubscriptions = this.subscriptionRepository.findInitiatedSubscriptions();

        for(Subscription s: expiredSubscriptions){
            if(s.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now())){
                logger.info("Promenjen status sa " + s.getStatus().toString() + " na EXPIRED");
                s.setStatus(SubscriptionStatus.EXPIRED);
                this.subscriptionRepository.save(s);
            }
        }

        List<Subscription> unfinishedSubscriptions = this.subscriptionRepository.findCreatedSubscriptions(); // traze se CREATED

        for(Subscription s : unfinishedSubscriptions){

            ResponseEntity<SubscriptionStatusDTO> response = null;
            try{
                response = restTemplate.getForEntity("https://localhost:8083/" + s.getPaymentMethod().getName().toLowerCase() + "-payment-service/api/subscriptionStatus?subscriptionId=" + s.getId(), SubscriptionStatusDTO.class);

            }catch(Exception e){
                e.printStackTrace();
                return;
            }

            if(response.getBody().getStatus() != null) {
                SubscriptionStatus status = SubscriptionStatus.valueOf(response.getBody().getStatus());
                if (!status.equals(s.getStatus())) {
                    logger.info("Promenjen status sa " + s.getStatus().toString() + " na " + status.toString());
                    s.setStatus(status);
                    this.subscriptionRepository.save(s);
                }
            }
        }
        logger.info("Updating subscription status finished...");
    }

    @Override
    public SubscriptionStatus findSubscriptionStatus(String merchantEmail, Long orderId) {

        Subscription subscription = this.subscriptionRepository.findSubscriptionForMerchant(merchantEmail, orderId);
        if(subscription == null){
            return null;
        }
        return subscription.getStatus();
    }
}
