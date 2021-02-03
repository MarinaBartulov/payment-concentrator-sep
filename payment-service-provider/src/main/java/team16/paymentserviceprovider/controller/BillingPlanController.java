package team16.paymentserviceprovider.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.service.BillingPlanService;
import team16.paymentserviceprovider.service.MerchantService;

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
}
