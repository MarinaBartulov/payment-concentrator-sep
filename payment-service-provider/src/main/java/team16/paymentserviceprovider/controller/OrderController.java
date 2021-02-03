package team16.paymentserviceprovider.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.paymentserviceprovider.dto.OrderDTO;
import team16.paymentserviceprovider.dto.OrderResponseDTO;
import team16.paymentserviceprovider.exceptions.InvalidDataException;
import team16.paymentserviceprovider.model.Order;
import team16.paymentserviceprovider.model.PaymentMethod;
import team16.paymentserviceprovider.service.OrderService;
import team16.paymentserviceprovider.service.PaymentMethodService;

@RestController
@RequestMapping(value = "/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentMethodService paymentMethodService;

    Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping
    public ResponseEntity<?> createOrderFromClientApp(@RequestBody OrderDTO dto) {

        try {
            OrderResponseDTO response = orderService.createOrderFromClientApp(dto);
            System.out.println("------------------------OrderResponseDTO from PSP to LA-------------------------------");
            System.out.println(response.getMerchantId());
            System.out.println(response.getOrderId());
            System.out.println(response.getRedirectionURL());
            logger.info("Order successfully created. Sending redirection URL");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidDataException ide) {
            logger.error("Invalid Data Exception whle creating order");
            System.out.println("Invalid Data Exception");
            return new ResponseEntity<>(ide.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Exception while creating order");
            System.out.println("Exception");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @GetMapping(value="/{id}/paymentMethods")
    public ResponseEntity getAvailablePaymentMethodsForOrder(@PathVariable("id") Long id){

        Order order = this.orderService.findById(id);
        if(order == null){
            return ResponseEntity.badRequest().body("Order with that id does not exist.");
        }
        return new ResponseEntity(this.orderService.getAvailablePaymentMethodsForOrder(order), HttpStatus.OK);
    }

    @PutMapping(value="/{id}/choosePaymentMethod")
    public ResponseEntity choosePaymentMethodForOrder(@PathVariable("id") Long id, @RequestBody String paymentMethodName){

        Order order = this.orderService.findById(id);
        if(order == null){
            return ResponseEntity.badRequest().body("Order with that id does not exist.");
        }

        PaymentMethod paymentMethod = this.paymentMethodService.findByName(paymentMethodName);
        if(paymentMethod == null){
            return ResponseEntity.badRequest().body("Payment method with that name does not exist.");
        }

        String redirectUrl = this.orderService.choosePaymentMethodForOrderAndSend(order, paymentMethodName);
        if(redirectUrl == null){
            return ResponseEntity.badRequest().body("Something went wrong while sending order to the payment service.");
        }else{
            return ResponseEntity.ok().body(redirectUrl);
        }

    }



}
