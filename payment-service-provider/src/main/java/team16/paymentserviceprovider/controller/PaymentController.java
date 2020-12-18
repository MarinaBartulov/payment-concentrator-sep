package team16.paymentserviceprovider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.paymentserviceprovider.dto.*;
import team16.paymentserviceprovider.service.PaymentService;

@RestController
@RequestMapping(value = "/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> getMerchantsInformationFromLA(@RequestBody OrderDTO dto) throws Exception {
        // proveri info o merchant-u sa onim sto imas u bazi
        System.out.println(dto.getMerchantId());
        OrderResponseDTO response = paymentService.createPaymentResponseToLA(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{orderId}")
    public ResponseEntity<?> createBankOrder(@PathVariable Long orderId) throws Exception{
        PaymentResponseInfoDTO response = paymentService.createPaymentRequest(orderId);
        return ResponseEntity.ok(response.getPaymentUrl());
    }

}
