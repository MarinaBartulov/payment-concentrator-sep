package team16.bankpaymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team16.bankpaymentservice.dto.PaymentRequestDTO;
import team16.bankpaymentservice.dto.PaymentResponseInfoDTO;
import team16.bankpaymentservice.service.PaymentService;

@RestController
@RequestMapping(value = "/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping(value = "/request")
    public ResponseEntity<?> createPaymentRequest(@RequestBody PaymentRequestDTO paymentRequestDTO) throws Exception {
        try {
            PaymentResponseInfoDTO paymentResponseInfoDTO = paymentService.generatePaymentInfo(paymentRequestDTO);
            return new ResponseEntity<>(paymentResponseInfoDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
