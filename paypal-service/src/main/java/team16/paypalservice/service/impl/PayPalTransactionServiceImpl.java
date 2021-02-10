package team16.paypalservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import team16.paypalservice.enums.PayPalTransactionStatus;
import team16.paypalservice.enums.SubscriptionStatus;
import team16.paypalservice.model.PayPalSubscription;
import team16.paypalservice.model.PayPalTransaction;
import team16.paypalservice.repository.PayPalTransactionRepository;
import team16.paypalservice.service.PayPalTransactionService;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class PayPalTransactionServiceImpl implements PayPalTransactionService {

    @Autowired
    PayPalTransactionRepository transactionRepository;

    Logger logger = LoggerFactory.getLogger(PayPalTransactionServiceImpl.class);

    @Override
    public PayPalTransaction save(PayPalTransaction transaction) {

        return transactionRepository.save(transaction);
    }

    @Override
    public PayPalTransaction findByPaymentId(String paymentId) {

        return transactionRepository.findByPaymentId(paymentId);
    }

    @Override
    public PayPalTransaction findById(Long transactionId) {

        return transactionRepository.findById(transactionId).orElse(null);
    }

    @Override
    public List<PayPalTransaction> findAllUnfinished() {
        return transactionRepository.findAllUnfinished();
    }

    @Override
    public PayPalTransaction findTransactionByOrderId(Long orderId) {
        return this.transactionRepository.findByOrderId(orderId);
    }

    @Override
    @Scheduled(cron = "0 0/5 * * * *")
    public void findUnfinishedTransactions() {
        logger.info("INITIATED FINDING UNFINISHED TRANSACTIONS");
        List<PayPalTransaction> transactionList = this.findAllUnfinished();

        for(PayPalTransaction transaction : transactionList)
        {
            if(transaction.getCreatedAt().plusMinutes(5).isBefore(LocalDateTime.now()))
            {
                logger.info("Transaction ID | " + transaction.getId()  +  " - status changed | EXPIRED");
                transaction.setStatus(PayPalTransactionStatus.EXPIRED);
                save(transaction);
            }
        }
    }




}
