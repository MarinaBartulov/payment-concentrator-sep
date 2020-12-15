package team16.bankpaymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/request")
    public PaymentResponseInfoDTO createPaymentRequest(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        System.out.println("Usao u banku na request");
        PaymentResponseInfoDTO dto = new PaymentResponseInfoDTO(1L, "uspeo hihi");
        return dto;
    }


}
