package team16.paypalservice.service;

import com.paypal.api.payments.*;
import com.paypal.api.payments.Currency;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team16.paypalservice.dto.OrderInfoDTO;
import team16.paypalservice.dto.SubscriptionDTO;
import team16.paypalservice.enums.PayPalTransactionStatus;
import team16.paypalservice.enums.SubscriptionFrequency;
import team16.paypalservice.enums.SubscriptionStatus;
import team16.paypalservice.model.Client;
import team16.paypalservice.model.PayPalSubscription;
import team16.paypalservice.model.PayPalTransaction;
import team16.paypalservice.service.impl.PayPalTransactionServiceImpl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PayPalService {

    @Autowired
    private PayPalTransactionServiceImpl transactionService;

    @Autowired
    private PayPalSubscriptionService subscriptionService;

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
            if (createdPayment.getState().equals("approved")) {
                transaction.setStatus(PayPalTransactionStatus.COMPLETED);
                logger.info("Saved transaction state | COMPLETED");
                transactionService.save(transaction);
                return createdPayment;
            }
            else{
                transaction.setStatus(PayPalTransactionStatus.FAILED);
                logger.info("Saved transaction state | FAILED");
                transactionService.save(transaction);
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

    public Long createBillingPlan(SubscriptionDTO subscriptionDTO, Client client, String SUBSCRIPTION_RETURN_URL, String SUBSCRIPTION_CANCEL_URL) throws PayPalRESTException {

        PayPalSubscription subscription = new PayPalSubscription(subscriptionDTO, client);
        PayPalSubscription savedSubscription = subscriptionService.save(subscription);
        logger.info("Subscription created | ID: " + savedSubscription.getId());

        //set currency and value
        Currency currency = new Currency();
        currency.setCurrency(subscriptionDTO.getCurrency());

        Double price = subscriptionDTO.getFrequency() == SubscriptionFrequency.YEAR ? 12*subscriptionDTO.getPrice() : subscriptionDTO.getPrice();
        price = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP).doubleValue();
        currency.setValue(price.toString());

        PaymentDefinition paymentDefinition = new PaymentDefinition();
        paymentDefinition.setName(client.getEmail() + " subscription");
        paymentDefinition.setType("REGULAR");
        paymentDefinition.setFrequency(subscriptionDTO.getFrequency().toString());
        paymentDefinition.setFrequencyInterval("1");
        paymentDefinition.setCycles(subscriptionDTO.getCyclesNumber().toString());
        paymentDefinition.setAmount(currency);

        List<PaymentDefinition> paymentDefinitionList = new ArrayList<PaymentDefinition>();
        paymentDefinitionList.add(paymentDefinition);

        MerchantPreferences merchantPreferences = new MerchantPreferences(SUBSCRIPTION_CANCEL_URL + savedSubscription.getId(), SUBSCRIPTION_RETURN_URL + savedSubscription.getId());
        merchantPreferences.setAutoBillAmount("YES");
        merchantPreferences.setInitialFailAmountAction("CONTINUE");

        //create a plan
        Plan plan = new Plan();
        plan.setType(subscriptionDTO.getType().toString());
        plan.setName(client.getEmail() + " subscription");
        plan.setDescription(subscriptionDTO.getPrice() + " " + subscriptionDTO.getCurrency() + " a " + subscriptionDTO.getFrequency().toString().toLowerCase());
        plan.setPaymentDefinitions(paymentDefinitionList);
        plan.setMerchantPreferences(merchantPreferences);

        APIContext context = new APIContext(client.getClientId(), client.getClientSecret(), mode);

        try {
            //create the plan

            Plan createdPlan = plan.create(context);
            logger.info("Billing plan created | ID: " + createdPlan.getId());
            //update plan state to ACTIVE
            List<Patch> patchRequestList = new ArrayList<Patch>();
            Map<String, String> value = new HashMap<String, String>();
            value.put("state", "ACTIVE");

            Patch patch = new Patch();
            patch.setPath("/");
            patch.setValue(value);
            patch.setOp("replace");
            patchRequestList.add(patch);

            //activate the plan
            createdPlan.update(context, patchRequestList);

            //save billing plan id
            savedSubscription.setBillingPlanId(createdPlan.getId());
        }
        catch (PayPalRESTException e) {
            savedSubscription.setStatus(SubscriptionStatus.FAILED);
            logger.info("Saved subscription status | FAILED");
        }

        subscriptionService.save(savedSubscription);

        return savedSubscription.getId();
    }

    public String createBillingAgreement(Client client, Long subscriptionId) throws PayPalRESTException, MalformedURLException, UnsupportedEncodingException {
        //get date for the agreement
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, 1);

        //format defined in ISO8601
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String formattedDate = sdf.format(c.getTime());

        PayPalSubscription subscription = subscriptionService.getOne(subscriptionId);
        logger.info("Subscription found | ID: " + subscription.getId());

        //set billing plan id
        Plan plan = new Plan();
        plan.setId(subscription.getBillingPlanId());

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        //create the agreement object
        Agreement agreement = new Agreement();
        agreement.setName(client.getEmail() + " subscription");
        agreement.setDescription(client.getEmail() + " subscription");
        agreement.setStartDate(formattedDate);

        agreement.setPlan(plan);
        agreement.setPayer(payer);

        APIContext context = new APIContext(client.getClientId(), client.getClientSecret(), mode);

        String redirectUrl = "";

        try {
            Agreement newAgreement = agreement.create(context);
            logger.info("Billing agreement created | ID: " + newAgreement.getId());

            if(newAgreement != null) {
                //get the approval url from the response
                Iterator links = newAgreement.getLinks().iterator();

                while(links.hasNext()) {
                    Links link = (Links) links.next();

                    if(link.getRel().equalsIgnoreCase("approval_url")) {
                        redirectUrl = link.getHref();
                        break;
                    }
                }
            }
        }
        catch (PayPalRESTException e) {
            throw e;
        }
        catch (MalformedURLException e) {
            throw e;
        }
        catch (UnsupportedEncodingException e) {
            throw e;
        }

        subscription.setStatus(SubscriptionStatus.CREATED);
        logger.info("Saved subscription status | CREATED");
        subscriptionService.save(subscription);

        //to redirect the customer to the paypal site
        return redirectUrl;
    }

    public void executeBillingAgreement(PayPalSubscription subscription, String token) throws PayPalRESTException {

        Agreement agreement =  new Agreement();
        agreement.setToken(token);

        Client client = subscription.getClient();

        APIContext context = new APIContext(client.getClientId(), client.getClientSecret(), mode);

        try {
            //execute the agreement and sign up the user for the subscription
            Agreement createdAgreement = agreement.execute(context, agreement.getToken());
            logger.info("Billing agreement executed | ID: " + createdAgreement.getId());
            subscription.setBillingAgreementId(createdAgreement.getId());
            subscription.setExecutedAt(LocalDateTime.now());
            subscription.setStatus(SubscriptionStatus.COMPLETED);
            logger.info("Saved subscription status | COMPLETED");
        }
        catch (PayPalRESTException e) {
            subscription.setStatus(SubscriptionStatus.FAILED);
            logger.info("Saved subscription status | FAILED");
            throw e;
        }

        subscriptionService.save(subscription);
    }

    public boolean cancelPayment(PayPalTransaction transaction)
    {
        if(transaction.getStatus().equals(PayPalTransactionStatus.INITIATED) || transaction.getStatus().equals(PayPalTransactionStatus.CREATED))
        {
            transaction.setStatus(PayPalTransactionStatus.CANCELED);
            transactionService.save(transaction);
            logger.info("Saved transaction status | CANCELED");

            return true;
        }
        logger.error("Can't change transaction status to CANCELED");
        return false;
    }

    public boolean cancelSubscription(PayPalSubscription subscription)
    {

        if(subscription.getStatus().equals(SubscriptionStatus.INITIATED) || subscription.getStatus().equals(SubscriptionStatus.CREATED))
        {
            subscription.setStatus(SubscriptionStatus.CANCELED);
            subscriptionService.save(subscription);
            logger.info("Saved subscription status | CANCELED");

            return true;
        }

        return false;
    }
}
