package team16.paypalservice.controller;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.paypalservice.dto.OrderInfoDTO;
import team16.paypalservice.dto.OrderStatusDTO;
import team16.paypalservice.dto.SubscriptionDTO;
import team16.paypalservice.dto.SubscriptionStatusDTO;
import team16.paypalservice.enums.PayPalTransactionStatus;
import team16.paypalservice.model.Client;
import team16.paypalservice.model.PayPalSubscription;
import team16.paypalservice.model.PayPalTransaction;
import team16.paypalservice.service.PayPalService;
import team16.paypalservice.service.impl.ClientServiceImpl;
import team16.paypalservice.service.impl.PayPalSubscriptionServiceImpl;
import team16.paypalservice.service.impl.PayPalTransactionServiceImpl;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class PayPalController {

    @Autowired
    private PayPalService payPalService;

    @Autowired
    private ClientServiceImpl clientService;

    @Autowired
    private PayPalTransactionServiceImpl transactionService;

    @Autowired
    private PayPalSubscriptionServiceImpl payPalSubscriptionService;

    public static final String RETURN_URL = "https://localhost:3001/pay/return";
    public static final String CANCEL_URL = "https://localhost:3001/pay/cancel/";
    public static final String FAIL_URL = "https://localhost:3001/pay/return/fail";
    public static final String SUCCESS_URL = "https://localhost:3001/pay/return/success";

    public static final String SUBSCRIPTION_RETURN_URL = "https://localhost:3001/subscription/return/";
    public static final String SUBSCRIPTION_CANCEL_URL = "https://localhost:3001/subscription/cancel/";
    public static final String SUBSCRIPTION_FAIL_URL = "https://localhost:3001/subscription/fail";
    public static final String SUBSCRIPTION_SUCCESS_URL = "https://localhost:3001/subscription/success";

    Logger logger = LoggerFactory.getLogger(PayPalController.class);

    @PostMapping("/pay")
    public ResponseEntity<?> createPayment(@RequestBody @Valid OrderInfoDTO order) {

        Client client = clientService.findByEmail(order.getMerchantEmail());
        if (client == null) {
            logger.error("Merchant not found | Email: " + order.getMerchantEmail());
            return new ResponseEntity<>(FAIL_URL, HttpStatus.OK);
        }

        String redirectUrl = "";
        try {
            logger.info("Creating payment | OrderId: " + order.getOrderId());
            redirectUrl = payPalService.createPayment(order, client, RETURN_URL, CANCEL_URL);
            logger.info("Created payment | OrderId: " + order.getOrderId());
        }
        catch (PayPalRESTException e) {
            logger.error("PayPal REST Exception occurred, payment not created");
            return new ResponseEntity<>(FAIL_URL, HttpStatus.OK);
        }

        return new ResponseEntity<>(redirectUrl, HttpStatus.OK);
    }

    @GetMapping("/pay/cancel")
    public ResponseEntity<?> cancelPayment(@RequestParam("id") Long transactionId) {

        PayPalTransaction transaction = transactionService.findById(transactionId);
        if(transaction == null)
        {
            logger.error("Transaction not found | ID: " + transactionId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Transaction found | ID: " + transactionId);

        if(payPalService.cancelPayment(transaction))
        {
            logger.info("Transaction canceled | ID: " + transactionId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/pay/execute")
    public ResponseEntity<?> executePayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {

        PayPalTransaction transaction = transactionService.findByPaymentId(paymentId);
        if(transaction == null)
        {
            logger.error("Transaction not found | PaymentId: " + paymentId);
            return new ResponseEntity<>(FAIL_URL, HttpStatus.OK);
        }
        logger.info("Transaction  found | PaymentId: " + paymentId);

        try {
            logger.info("Executing payment | PaymentId: " + paymentId);
            Payment payment = payPalService.executePayment(paymentId, payerId, transaction);
            System.out.println(payment.toJSON());
            logger.info("Payment executed | PaymentID: " + paymentId);

            if (payment.getState().equals("approved")) {
                return new ResponseEntity<>(SUCCESS_URL, HttpStatus.OK);
            }
        } catch (PayPalRESTException e) {
            logger.error("Failed executing payment | PaymentId: " + paymentId);
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(FAIL_URL, HttpStatus.OK);
    }

    @PostMapping("/subscription/create")
    public ResponseEntity<?> createSubscription(@RequestBody @Valid SubscriptionDTO subscriptionDTO) {

        Client client = clientService.findByEmail(subscriptionDTO.getEmail());
        if(client == null) {
            logger.error("Merchant not found | Email: " + subscriptionDTO.getEmail());
            return new ResponseEntity<>(SUBSCRIPTION_FAIL_URL, HttpStatus.OK);
        }
        logger.info("Merchant found | Email: " + subscriptionDTO.getEmail());

        Long subscriptionId;
        try {
            subscriptionId = payPalService.createBillingPlan(subscriptionDTO, client, SUBSCRIPTION_RETURN_URL, SUBSCRIPTION_CANCEL_URL);
        }
        catch (PayPalRESTException e) {
            logger.error("PayPal REST Exception occurred, billing plan not created");
            return new ResponseEntity<>(SUBSCRIPTION_FAIL_URL, HttpStatus.OK);
        }

        String redirectUrl = "";
        try {
            redirectUrl = payPalService.createBillingAgreement(client, subscriptionId);
        }
        catch (PayPalRESTException e) {
            logger.error("PayPal REST Exception occurred, billing agreement not created");
            return new ResponseEntity<>(SUBSCRIPTION_FAIL_URL, HttpStatus.OK);
        }
        catch (Exception e) {
            logger.error("Exception occurred, billing agreement not created");
            return new ResponseEntity<>(SUBSCRIPTION_FAIL_URL, HttpStatus.OK);
        }

        return new ResponseEntity<>(redirectUrl, HttpStatus.OK);
    }

    @GetMapping(value = "/subscription/execute")
    public ResponseEntity<?> executeSubscription(@RequestParam Long subscriptionId, @RequestParam String token) {

        PayPalSubscription subscription = payPalSubscriptionService.getOne(subscriptionId);
        if(subscription == null)
        {
            logger.error("Subscription not found | SubscriptionId: " + subscriptionId);
            return new ResponseEntity<>(SUBSCRIPTION_FAIL_URL, HttpStatus.OK);
        }

        try {
            payPalService.executeBillingAgreement(subscription, token);
        }
        catch(PayPalRESTException e) {
            logger.error("CANCELED | PayPal Subscription Execution");
            return new ResponseEntity<>(SUBSCRIPTION_FAIL_URL, HttpStatus.OK);
        }

        logger.info("COMPLETED | PayPal Subscription Execution");

        return new ResponseEntity<>(SUBSCRIPTION_SUCCESS_URL, HttpStatus.OK);
    }

    @GetMapping("/subscription/cancel")
    public ResponseEntity<?> cancelSubscription(@RequestParam Long subscriptionId) {

        PayPalSubscription subscription = payPalSubscriptionService.getOne(subscriptionId);
        if(subscription == null)
        {
            logger.error("Subscription not found | ID: " + subscriptionId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (payPalService.cancelSubscription(subscription))
        {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/status")
    public ResponseEntity checkTransactionStatus(@RequestParam("orderId") Long orderId){

        PayPalTransaction tr = this.transactionService.findTransactionByOrderId(orderId);
        if(tr == null){
            logger.error("Transaction with orderId " + orderId + " doesn't exist.");
            return ResponseEntity.badRequest().body("Transaction with orderId " + orderId + " doesn't exist.");
        }

        OrderStatusDTO orderStatusDTO = new OrderStatusDTO();
        if(tr.getStatus() == PayPalTransactionStatus.INITIATED || tr.getStatus() == PayPalTransactionStatus.CREATED){
            orderStatusDTO.setStatus("CREATED");
        }else if(tr.getStatus() == PayPalTransactionStatus.FAILED){
            orderStatusDTO.setStatus("INVALID");
        }else{
            orderStatusDTO.setStatus(tr.getStatus().toString());
        }
        return new ResponseEntity(orderStatusDTO, HttpStatus.OK);
    }

    @GetMapping("/subscriptionStatus")
    public ResponseEntity<?> checkSubscriptionStatus(@RequestParam("subscriptionId") Long subscriptionId){

        PayPalSubscription s = this.payPalSubscriptionService.findBySubscriptionId(subscriptionId);
        if(s == null){
            logger.error("Subscription with SubscriptionId " + subscriptionId + " doesn't exist.");
            return ResponseEntity.badRequest().body("Subscription with SubscriptionId " + subscriptionId + " doesn't exist.");
        }
        logger.info("Checking for subscription status | ID: " + subscriptionId);
        SubscriptionStatusDTO subscriptionStatusDTO = new SubscriptionStatusDTO();
        subscriptionStatusDTO.setStatus(s.getStatus().toString());
        return new ResponseEntity<>(subscriptionStatusDTO, HttpStatus.OK);
    }
}
