package team16.paymentserviceprovider.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.paymentserviceprovider.dto.SubscriptionRequestDTO;
import team16.paymentserviceprovider.dto.SubscriptionResponseDTO;
import team16.paymentserviceprovider.model.BillingPlan;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.model.PaymentMethod;
import team16.paymentserviceprovider.model.Subscription;
import team16.paymentserviceprovider.service.BillingPlanService;
import team16.paymentserviceprovider.service.MerchantService;
import team16.paymentserviceprovider.service.impl.SubscriptionServiceImpl;

import java.net.URISyntaxException;

@RestController
@RequestMapping(value = "/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionServiceImpl subscriptionService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private BillingPlanService billingPlanService;

    Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    @PostMapping(value="/subscribe")
    public ResponseEntity<?> saveSubscriptionFromLA(@RequestBody SubscriptionRequestDTO dto) {
        System.out.println("Usao u save subscription from LA");

        Merchant merchant = merchantService.findByMerchantEmail(dto.getMerchantEmail());
        if(merchant == null)
        {
            logger.error("Merchant not found | Email: " + dto.getMerchantEmail());
            return new ResponseEntity<>("Merchant not found", HttpStatus.BAD_REQUEST);
        }
        logger.info("Found merchant: " + merchant.getEmail() + " | " + merchant.getMerchantId());

        Boolean hasPayPal = false;
        for(PaymentMethod paymentMethod : merchant.getPaymentMethods())
        {
            if(paymentMethod.getName().equals("PayPal")){
                hasPayPal = true;
                break;
            }
        }
        if(!hasPayPal){
            logger.error("Merchant doesn't have payment method for paying subscription");
            return new ResponseEntity<>("Merchant doesn't have payment method for paying subscription", HttpStatus.EXPECTATION_FAILED);
        }

        BillingPlan billingPlan = billingPlanService.getOne(dto.getBillingPlanId());
        if(billingPlan == null)
        {
            logger.error("Billing plan not found | ID: " + dto.getBillingPlanId());
            return new ResponseEntity<>("Billing plan not found", HttpStatus.BAD_REQUEST);
        }
        logger.info("Found billing plan | ID " + dto.getBillingPlanId());

        try {
            SubscriptionResponseDTO subscriptionResponseDTO = subscriptionService.createSubscriptionFromLA(dto, merchant, billingPlan);
            logger.info("Subscription successfully created. Sending redirection URL to LA");
            System.out.println(subscriptionResponseDTO.getRedirectURL());
            return new ResponseEntity<>(subscriptionResponseDTO, HttpStatus.OK);
        }catch (Exception e) {
            logger.error("Exception while creating subscription");
            return new ResponseEntity<>("Error when creating subscription", HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping(value="/subscription/{subscriptionId}")
    public ResponseEntity<?> createSubscription(@PathVariable Long subscriptionId) throws URISyntaxException {
        Subscription subscription = subscriptionService.getOne(subscriptionId);
        logger.info("Found subscription | ID: " + subscription.getId());
        if(subscription == null)
        {
            logger.error("Subscription not found | ID: " + subscriptionId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String redirectUrl = subscriptionService.createSubscription(subscription);
        if(redirectUrl == null)
        {
            logger.error("Failed to get URL");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Sending redirection URL");
        System.out.println("Redirect url = " + redirectUrl);
        return new ResponseEntity<>(redirectUrl, HttpStatus.OK);
    }


}
