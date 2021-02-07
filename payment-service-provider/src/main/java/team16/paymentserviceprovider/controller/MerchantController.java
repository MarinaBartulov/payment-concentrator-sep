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
import team16.paymentserviceprovider.dto.MerchantBankDTO;
import team16.paymentserviceprovider.dto.MerchantPCDTO;
import team16.paymentserviceprovider.model.App;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.model.PaymentMethod;
import team16.paymentserviceprovider.service.AppService;
import team16.paymentserviceprovider.service.MerchantService;
import team16.paymentserviceprovider.service.PaymentMethodService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/merchant")
public class MerchantController {

    @Autowired
    private AppService appService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private PaymentMethodService paymentMethodService;

    Logger logger = LoggerFactory.getLogger(MerchantController.class);



    @PostMapping
    public ResponseEntity registerNewMerchant(@RequestBody @Valid MerchantPCDTO merchantPCDTO) throws MessagingException, InterruptedException {

        logger.info("Merchant registration initiated.");
        App app = this.appService.findByAppId(merchantPCDTO.getAppId());
        if(app == null){
            logger.warn("App with id " + merchantPCDTO.getAppId() + " doesn't exist.");
            return ResponseEntity.notFound().build();
        }
        Merchant m = this.merchantService.findByMerchantEmail(merchantPCDTO.getMerchantEmail());
        if(m != null){
            logger.warn("Merchant with email " + merchantPCDTO.getMerchantEmail() + " already exist.");
            return ResponseEntity.badRequest().body("Merchant with this email already exists.");
        }

        if(merchantService.registerNewMerchant(merchantPCDTO)){
            logger.info("Merchant registration successful. Merchant's email: " + merchantPCDTO.getMerchantEmail());
            return new ResponseEntity(merchantPCDTO, HttpStatus.OK);
        }else{
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping(value="/info")
    @PreAuthorize("hasAuthority('merchant_info')")
    public ResponseEntity<?> getMyInfo(){

        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Getting merchant's info. Merchant's email: " + currentUser.getName());
        return new ResponseEntity(this.merchantService.getMyInfo(currentUser), HttpStatus.OK);
    }

    @GetMapping(value="/current")
    public ResponseEntity<?> getCurrentMerchant(){

        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Getting current merchant. Merchant's email: " + currentUser.getName());
        return new ResponseEntity(currentUser.getName(), HttpStatus.OK);
    }

    @PostMapping(value="/paymentMethod/{paymentMethodName}")
    @PreAuthorize("hasAuthority('payment_method_add')")
    public ResponseEntity<?> addPaymentMethodForCurrentMerchant(@RequestHeader("Authorization") String authToken, @PathVariable("paymentMethodName") String paymentMethodName,
                                                                @RequestBody Map<String, Object> formValues){

        PaymentMethod paymentMethod = this.paymentMethodService.findByName(paymentMethodName);
        if(paymentMethod == null){
            logger.warn("Payment method with name " + paymentMethodName + " doesn't exist.");
            return ResponseEntity.badRequest().body("Payment method with that name does not exist.");
        }

        String errorMsg = this.merchantService.addPaymentMethodForCurrentMerchant(authToken, paymentMethodName, formValues);
        if(errorMsg == null){
            logger.info("Payment method successfully added for the merchant.");
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.badRequest().body(errorMsg);
        }
    }

    @PostMapping(value = "/save-info-from-bank")
    public ResponseEntity<?> saveMerchantInfoFromBank(@RequestBody MerchantBankDTO dto) {
        try {
            Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Auth sa Banke: " + currentUser.getName());
            return new ResponseEntity(merchantService.updateMerchant(currentUser.getName(), dto), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }
}
