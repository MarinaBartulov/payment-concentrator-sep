package team16.paymentserviceprovider.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import team16.paymentserviceprovider.dto.AppDTO;
import team16.paymentserviceprovider.dto.BillingPlanDTO;
import team16.paymentserviceprovider.model.BillingPlan;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.model.User;
import team16.paymentserviceprovider.security.auth.JwtBasedAuthentication;
import team16.paymentserviceprovider.service.BillingPlanService;
import team16.paymentserviceprovider.service.MerchantService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/billing-plans")
public class BillingPlanController {

    @Autowired
    private BillingPlanService billingPlanService;

    @Autowired
    private MerchantService merchantService;

    Logger logger = LoggerFactory.getLogger(BillingPlanController.class);

    @PostMapping
    public ResponseEntity<?> getAllBillingPlansForMerchant(@RequestBody String email){
        System.out.println("Usao u get all billing plans for merchant");

        Merchant merchant = merchantService.findByMerchantEmail(email);
        if(merchant == null)
        {
            logger.error("Merchant not found | Email: " + email);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Found merchant | Email: " + merchant.getEmail());

        return new ResponseEntity<>(billingPlanService.getAllBillingPlansForMerchant(merchant.getId()),HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('billing_plan_create')")
    public ResponseEntity<?> createBillingPlan(@RequestBody @Valid BillingPlanDTO billingPlanDTO){
        System.out.println("Usao u create billing plan");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JwtBasedAuthentication jwtBasedAuthentication = (JwtBasedAuthentication) auth;
        Merchant merchant = (Merchant) jwtBasedAuthentication.getPrincipal();
        if(merchant == null)
        {
            logger.error("Merchant not found" );
            return new ResponseEntity<>("Authentication failed", HttpStatus.BAD_REQUEST);
        }

        BillingPlan billingPlan = billingPlanService.create(billingPlanDTO, merchant);
        logger.info("Billing plan created for merchant | Email: " + merchant.getEmail());
        if(billingPlan == null){
            return new ResponseEntity<>("Failed to create billing plan",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
