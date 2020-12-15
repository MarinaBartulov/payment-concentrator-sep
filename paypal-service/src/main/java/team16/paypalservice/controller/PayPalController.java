package team16.paypalservice.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.paypalservice.dto.Order;
import team16.paypalservice.service.PayPalService;

@RestController
@RequestMapping(value = "/api")
public class PayPalController {

    @Autowired
    private PayPalService payPalService;

    //promeniti na https
    public static final String BASE_URL = "http://localhost:3001/";
    public static final String RETURN_URL = "pay/return";
    public static final String CANCEL_URL = "pay/cancel";

    @PostMapping("/pay")
    public ResponseEntity<?> payment(@RequestBody Order order) {
        try {
            System.out.println(order.getPrice());
            Payment payment = payPalService.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
                    order.getIntent(), order.getDescription(), BASE_URL + CANCEL_URL,
                    BASE_URL + RETURN_URL);
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return new ResponseEntity(link.getHref(), HttpStatus.OK);
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

   /* @GetMapping("pay/cancel")
    public String cancelPay() {
        return "cancel";
    }*/

    @GetMapping("/pay/execute")
    public ResponseEntity<?> executePayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            System.out.println("USAO U EXECUTE PAYMENT");
            Payment payment = payPalService.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                return new ResponseEntity<>("approved",HttpStatus.OK);
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
            //return new ResponseEntity<>("failed",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("failed",HttpStatus.BAD_REQUEST);
    }
}
