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
import team16.paypalservice.enums.PayPalTransactionStatus;
import team16.paypalservice.model.Client;
import team16.paypalservice.model.PayPalTransaction;
import team16.paypalservice.service.PayPalService;
import team16.paypalservice.service.impl.ClientServiceImpl;
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

    public static final String RETURN_URL = "https://localhost:3001/pay/return";
    public static final String CANCEL_URL = "https://localhost:3001/pay/cancel/";
    public static final String FAIL_URL = "https://localhost:3001/pay/return/fail";
    public static final String SUCCESS_URL = "https://localhost:3001/pay/return/success";

    Logger logger = LoggerFactory.getLogger(PayPalController.class);

    @PostMapping("/pay")
    public ResponseEntity<?> payment(@RequestBody @Valid OrderInfoDTO order) {

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
    public ResponseEntity<?> cancelPay(@RequestParam("id") Long transactionId) {

        PayPalTransaction transaction = transactionService.findById(transactionId);
        if(transaction == null)
        {
            logger.error("Transaction not found | ID: " + transactionId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        transaction.setStatus(PayPalTransactionStatus.CANCELED);
        transactionService.save(transaction);
        logger.info("Saved transaction state | CANCELED");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/pay/execute")
    public ResponseEntity<?> executePayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {

        PayPalTransaction transaction = transactionService.findByPaymentId(paymentId);

        if(transaction == null)
        {
            logger.error("Transaction not found | PaymentId: " + paymentId);
            return new ResponseEntity<>(FAIL_URL, HttpStatus.BAD_REQUEST);
        }

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
        return new ResponseEntity<>(FAIL_URL, HttpStatus.BAD_REQUEST);
    }
}
