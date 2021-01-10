package team16.paymentserviceprovider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(value="/formsData")
    public ResponseEntity getFormsDataForAvailablePaymentMethodsForCurrentMerchant(){
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        List<FormDataDTO> formsDataDTO = this.paymentMethodService.getFormsDataForAvailablePaymentMethodsForCurrentMerchant(currentUser);
        if(formsDataDTO == null){
            return ResponseEntity.badRequest().body("Error occurred while retrieving form data from payment services");
        }
        return new ResponseEntity(formsDataDTO, HttpStatus.OK);

    }

}
