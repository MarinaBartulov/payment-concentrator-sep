package team16.paymentserviceprovider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.paymentserviceprovider.dto.PaymentDetailsDTO;
import team16.paymentserviceprovider.dto.PaymentResponseInfoDTO;
import team16.paymentserviceprovider.dto.ResponseForLADTO;
import team16.paymentserviceprovider.service.PaymentService;

@RestController
@RequestMapping(value = "/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> getMerchantsInformationFromLA(@RequestBody PaymentDetailsDTO dto) throws Exception {
        // proveri info o merchant-u sa onim sto imas u bazi
        System.out.println(dto.getMerchantId());
        System.out.println("Success");
        ResponseForLADTO response = paymentService.createPaymentResponseToLA(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> createBankOrder() throws Exception{
        PaymentResponseInfoDTO response = paymentService.createPaymentRequest();
        return ResponseEntity.ok(response.getPayment_url());
    }

}
