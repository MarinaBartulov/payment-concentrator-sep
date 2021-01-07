package team16.paymentserviceprovider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team16.paymentserviceprovider.dto.MerchantPCDTO;
import team16.paymentserviceprovider.model.App;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.service.AppService;
import team16.paymentserviceprovider.service.MerchantService;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/merchant")
public class MerchantController {

    @Autowired
    private AppService appService;
    @Autowired
    private MerchantService merchantService;


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
}
