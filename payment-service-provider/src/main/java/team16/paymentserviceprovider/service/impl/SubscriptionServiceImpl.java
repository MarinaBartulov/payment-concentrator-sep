package team16.paymentserviceprovider.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import team16.paymentserviceprovider.config.EndpointConfig;
import team16.paymentserviceprovider.config.RestConfig;
import team16.paymentserviceprovider.dto.SubscriptionInfoDTO;
import team16.paymentserviceprovider.dto.SubscriptionRequestDTO;
import team16.paymentserviceprovider.dto.SubscriptionResponseDTO;
import team16.paymentserviceprovider.model.BillingPlan;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.model.Subscription;
import team16.paymentserviceprovider.repository.SubscriptionRepository;
import team16.paymentserviceprovider.service.SubscriptionService;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private BillingPlanServiceImpl billingPlanService;

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
        logger.info("Found Merchant | ID: " + merchant.getId());
        if(merchant == null){
            logger.error("Failed to find Merchant | ID: " + subscription.getMerchant().getMerchantId());
            return null;
        }

        BillingPlan billingPlan = billingPlanService.getOne(subscription.getBillingPlan().getId());
        if(billingPlan == null)
        {
            logger.error("Failed to find Billing plan | ID: " + subscription.getBillingPlan().getId());
            return null;
        }
        logger.info("Found Billing plan ID | " + billingPlan.getId());

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
}
