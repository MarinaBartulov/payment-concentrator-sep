package team16.paymentserviceprovider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
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


    @PostMapping
    public ResponseEntity registerNewMerchant(@RequestBody @Valid MerchantPCDTO merchantPCDTO) throws MessagingException, InterruptedException {
        System.out.println("Uslo ovde");
        App app = this.appService.findByAppId(merchantPCDTO.getAppId());
        if(app == null){
            return ResponseEntity.notFound().build();
        }
        Merchant m = this.merchantService.findByMerchantEmail(merchantPCDTO.getMerchantEmail());
        if(m != null){
            return ResponseEntity.badRequest().body("Merchant with this email already exists.");
        }

        if(merchantService.registerNewMerchant(merchantPCDTO)){
            return new ResponseEntity(merchantPCDTO, HttpStatus.OK);
        }else{
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping(value="/info")
    public ResponseEntity<?> getMyInfo(){

        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity(this.merchantService.getMyInfo(currentUser), HttpStatus.OK);
    }

    @GetMapping(value="/current")
    public ResponseEntity<?> getCurrentMerchant(){

        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity(currentUser.getName(), HttpStatus.OK);
    }

    @PostMapping(value="/paymentMethod/{paymentMethodName}")
    public ResponseEntity<?> addPaymentMethodForCurrentMerchant(@RequestHeader("Authorization") String authToken, @PathVariable("paymentMethodName") String paymentMethodName,
                                                                @RequestBody Map<String, Object> formValues){

        PaymentMethod paymentMethod = this.paymentMethodService.findByName(paymentMethodName);
        if(paymentMethod == null){
            return ResponseEntity.badRequest().body("Payment method with that name does not exist.");
        }

        String errorMsg = this.merchantService.addPaymentMethodForCurrentMerchant(authToken, paymentMethodName, formValues);
        if(errorMsg == null){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.badRequest().body(errorMsg);
        }
    }
}
