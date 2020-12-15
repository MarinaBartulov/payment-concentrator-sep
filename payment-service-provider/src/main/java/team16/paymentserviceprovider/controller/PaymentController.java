package team16.paymentserviceprovider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team16.paymentserviceprovider.dto.PaymentDetailsDTO;
import team16.paymentserviceprovider.service.PaymentService;

@RestController
@RequestMapping(value = "/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    ResponseEntity<?> getMerchantsInformationFromLA(@RequestBody PaymentDetailsDTO dto) throws Exception {
        // proveri info o merchant-u sa onim sto imas u bazi
        System.out.println(dto.getMerchantId());
        System.out.println("Success");
        paymentService.createPaymentRequest(dto);
        return ResponseEntity.ok("http://localhost:3001");

    }
}
