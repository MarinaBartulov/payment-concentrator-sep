package team16.bankpaymentservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import team16.bankpaymentservice.enums.TransactionStatus;
import team16.bankpaymentservice.model.Transaction;
import team16.bankpaymentservice.repository.TransactionRepository;
import team16.bankpaymentservice.service.TransactionService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Override
    public Transaction findById(Long id) {
        return transactionRepository.getOne(id);
    }

    @Override
    public Transaction findByAcquirerOrderId(Long id) {
        return transactionRepository.findTransactionByAcquirerOrderId(id);
    }

    @Override
    public Transaction create(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction update(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction findByMerchantOrderId(Long id) {
        return this.transactionRepository.findTransactionByMerchantOrderId(id);
    }

    @Override
    @Scheduled(cron = "0 0/15 * * * *")
    public void updateUnfinishedTransactionsFromBankSite() {
        //pronadju se u bazi transakcije koje su INITIATED, CREATED

        List<Transaction> transactions = this.transactionRepository.findUnfinishedTransactions();

        if(transactions.size() > 0){
            logger.info("Changing non-finished transactions status to FAILED");
        }

        for(Transaction t : transactions){
            if(t.getMerchantTimestamp().plusMinutes(30).isBefore(LocalDateTime.now())) {
                t.setStatus(TransactionStatus.FAILED);
                transactionRepository.save(t);
                logger.info("Transaction " + t.getId() + " status changed to FAILED");
            }
        }


    }
}
