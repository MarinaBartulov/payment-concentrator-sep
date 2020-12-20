package team16.paypalservice.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team16.paypalservice.dto.OrderInfoDTO;
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

    Logger logger = LoggerFactory.getLogger(PayPalService.class);

    public String createPayment(OrderInfoDTO order, Client client, String RETURN_URL, String CANCEL_URL) throws PayPalRESTException {

        PayPalTransaction payPalTransaction = new PayPalTransaction(order, client);
        PayPalTransaction savedPayPalTransaction = transactionService.save(payPalTransaction);
        logger.info("Created transaction | ID: " + savedPayPalTransaction.getId());

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Amount amount = new Amount();
        amount.setCurrency(order.getCurrency());
        amount.setTotal(order.getAmount().toString());

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
            logger.info("Creating payment for merchant | Email: " + order.getMerchantEmail());
            Payment newPayment = payment.create(context);
            logger.info("Created payment for merchant | Email: "+ order.getMerchantEmail());

            for(Links link:newPayment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    redirectUrl = link.getHref();
                }
            }
            System.out.println(redirectUrl);
            savedPayPalTransaction.setPaymentId(newPayment.getId());
        }
        catch (PayPalRESTException e) {
            logger.error("Failed creating PayPal payment");
            savedPayPalTransaction.setStatus(PayPalTransactionStatus.FAILED);
            transactionService.save(savedPayPalTransaction);
            logger.info("Saved transaction status | FAILED");
            throw e;
        }

        savedPayPalTransaction.setStatus(PayPalTransactionStatus.CREATED);
        transactionService.save(savedPayPalTransaction);
        logger.info("Saved transaction status | CREATED");

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
            transaction.setExecutedAt(LocalDateTime.now());
            transactionService.save(transaction);
            if (createdPayment.getState().equals("approved")) {
                transaction.setStatus(PayPalTransactionStatus.COMPLETED);
                logger.info("Saved transaction state | COMPLETED");
                return createdPayment;
            }
            else{
                transaction.setStatus(PayPalTransactionStatus.FAILED);
                logger.info("Saved transaction state | FAILED");
                return createdPayment;
            }

        }
        catch (PayPalRESTException e) {
            logger.error("Failed executing PayPal payment");
            transaction.setStatus(PayPalTransactionStatus.FAILED);
            transactionService.save(transaction);
            logger.info("Saved transaction status | FAILED");

            throw e;
        }
    }
}
