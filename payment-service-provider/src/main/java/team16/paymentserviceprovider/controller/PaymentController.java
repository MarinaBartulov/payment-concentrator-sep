package team16.paymentserviceprovider.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.paymentserviceprovider.dto.*;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.model.Order;
import team16.paymentserviceprovider.service.OrderService;
import team16.paymentserviceprovider.service.PaymentService;

import java.net.URISyntaxException;

@RestController
@RequestMapping(value = "/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    Logger logger = LoggerFactory.getLogger(PaymentController.class);

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

    @PutMapping(value="/{orderId}")
    public ResponseEntity<?> createGenericOrder(@PathVariable Long orderId, @RequestBody String paymentMethodName) throws URISyntaxException {
        Order order = orderService.getOne(orderId);

        if(order == null)
        {
            logger.error("Order not found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //proveriti payment method

        String redirectUrl = paymentService.createGenericPaymentRequest(order, paymentMethodName);
        if(redirectUrl == null)
        {
            logger.error("Failed to get URL");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Sending redirection URL");
        System.out.println("Redirect url = " + redirectUrl);
        return new ResponseEntity<>(redirectUrl, HttpStatus.OK);
    }

}
