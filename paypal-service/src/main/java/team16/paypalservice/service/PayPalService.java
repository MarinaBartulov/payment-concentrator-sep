package team16.paypalservice.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team16.paypalservice.dto.OrderDTO;
import team16.paypalservice.enums.PayPalTransactionStatus;
import team16.paypalservice.model.Client;
import team16.paypalservice.model.PayPalTransaction;
import team16.paypalservice.service.impl.PayPalTransactionServiceImpl;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PayPalService {

    @Autowired
    private PayPalTransactionServiceImpl transactionService;

    @Value("${paypal.mode}")
    private String mode;

    public String createPayment(OrderDTO order, Client client, String RETURN_URL, String CANCEL_URL) throws PayPalRESTException {

        PayPalTransaction payPalTransaction = new PayPalTransaction(order, client);
        PayPalTransaction savedPayPalTransaction = transactionService.save(payPalTransaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Amount amount = new Amount();
        amount.setCurrency(order.getCurrency());
        amount.setTotal(order.getPrice().toString());

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(CANCEL_URL + savedPayPalTransaction.getId());
        redirectUrls.setReturnUrl(RETURN_URL);

        Transaction transaction = new Transaction();
        transaction.setDescription("Payment for client with email: " + client.getEmail());
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(redirectUrls);

        APIContext context = new APIContext(client.getClientId(), client.getClientSecret(), mode);

        String redirectUrl = "";

        try {
            Payment newPayment = payment.create(context);

            for(Links link:newPayment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    redirectUrl = link.getHref();
                }
            }
            System.out.println(redirectUrl);
            savedPayPalTransaction.setPaymentId(newPayment.getId());
        }
        catch (PayPalRESTException e) {
            savedPayPalTransaction.setStatus(PayPalTransactionStatus.FAILED);
            transactionService.save(savedPayPalTransaction);
            throw e;
        }

        savedPayPalTransaction.setStatus(PayPalTransactionStatus.CREATED);
        transactionService.save(savedPayPalTransaction);

        return redirectUrl;
    }

    public Payment executePayment(String paymentId, String payerId, PayPalTransaction transaction) throws PayPalRESTException{

        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);

        Client client = transaction.getClient();

        APIContext context = new APIContext(client.getClientId(), client.getClientSecret(), mode);

        try {
            Payment createdPayment = payment.execute(context, paymentExecute);
            transaction.setStatus(PayPalTransactionStatus.COMPLETED);
            transaction.setExecutedAt(LocalDateTime.now());
            transactionService.save(transaction);
            return createdPayment;
        }
        catch (PayPalRESTException e) {
            transaction.setStatus(PayPalTransactionStatus.FAILED);
            transactionService.save(transaction);
            throw e;
        }
    }
}
