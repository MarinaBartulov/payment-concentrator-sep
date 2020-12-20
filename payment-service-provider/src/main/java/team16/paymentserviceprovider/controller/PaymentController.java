package team16.paymentserviceprovider.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.paymentserviceprovider.dto.*;
import team16.paymentserviceprovider.exceptions.InvalidDataException;

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

        try {
            OrderResponseDTO response = paymentService.createPaymentResponseToLA(dto);
            System.out.println("------------------------OrderResponseDTO from PSP to LA-------------------------------");
            System.out.println(response.getMerchantId());
            System.out.println(response.getOrderId());
            System.out.println(response.getRedirectionURL());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidDataException ide) {
            System.out.println("Invalid Data Exception");
            return new ResponseEntity<>(ide.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("Exception");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value = "/{orderId}")
    public ResponseEntity<?> createBankOrder(@PathVariable Long orderId) throws Exception{
        try {
            PaymentResponseInfoDTO response = paymentService.createPaymentRequest(orderId);
            System.out.println("Response OK");
            return ResponseEntity.ok(response.getPaymentUrl());
        } catch (InvalidDataException ide) {
            System.out.println("Response Invalid Data Exception");
            return new ResponseEntity<>(ide.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            System.out.println("Response Exception");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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
