package team16.paymentserviceprovider.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team16.paymentserviceprovider.dto.FormDataDTO;
import team16.paymentserviceprovider.service.PaymentMethodService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/paymentMethod")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    Logger logger = LoggerFactory.getLogger(PaymentMethodController.class);


    @GetMapping(value="/formsData")
    @PreAuthorize("hasAuthority('pm_data_read')")
    public ResponseEntity getFormsDataForAvailablePaymentMethodsForCurrentMerchant(){
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        List<FormDataDTO> formsDataDTO = this.paymentMethodService.getFormsDataForAvailablePaymentMethodsForCurrentMerchant(currentUser);
        if(formsDataDTO == null){
            logger.error("Error occurred while retrieving form data from payment services");
            return ResponseEntity.badRequest().body("Error occurred while retrieving form data from payment services");
        }
        return new ResponseEntity(formsDataDTO, HttpStatus.OK);

    }

    @GetMapping
    @PreAuthorize("hasAuthority('pm_all_read')")
    public ResponseEntity getAllPaymentMethods(){

        logger.info("Getting all payment methods.");
        return ResponseEntity.ok(this.paymentMethodService.getAllPaymentMethods());
    }

}
