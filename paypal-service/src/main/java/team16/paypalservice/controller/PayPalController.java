package team16.paypalservice.controller;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @PostMapping("/pay")
    public ResponseEntity<?> payment(@RequestBody @Valid OrderInfoDTO order) {

        System.out.println(order.getMerchantEmail());
        Client client = clientService.findByEmail(order.getMerchantEmail());
        if (client == null) {
            return new ResponseEntity<>(FAIL_URL, HttpStatus.OK);
        }

        String redirectUrl = "";
        try {
            redirectUrl = payPalService.createPayment(order, client, RETURN_URL, CANCEL_URL);
        }
        catch (PayPalRESTException e) {
            return new ResponseEntity<>(FAIL_URL, HttpStatus.OK);
        }

        return new ResponseEntity<>(redirectUrl, HttpStatus.OK);
    }

    @GetMapping("/pay/cancel")
    public ResponseEntity<?> cancelPay(@RequestParam("id") Long transactionId) {

        PayPalTransaction transaction = transactionService.findById(transactionId);
        if(transaction == null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        transaction.setStatus(PayPalTransactionStatus.CANCELED);
        transactionService.save(transaction);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/pay/execute")
    public ResponseEntity<?> executePayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {

        PayPalTransaction transaction = transactionService.findByPaymentId(paymentId);

        try {
            Payment payment = payPalService.executePayment(paymentId, payerId, transaction);
            System.out.println(payment.toJSON());

            if (payment.getState().equals("approved")) {
                return new ResponseEntity<>("approved", HttpStatus.OK);
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
    }
}
