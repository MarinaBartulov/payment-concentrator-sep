package team16.bitcoinservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team16.bitcoinservice.dto.BitcoinPaymentDTO;
import team16.bitcoinservice.dto.PaymentRequestDTO;
import team16.bitcoinservice.dto.PaymentResponseDTO;
import team16.bitcoinservice.model.Merchant;
import team16.bitcoinservice.model.Transaction;
import team16.bitcoinservice.model.TransactionStatus;
import team16.bitcoinservice.repository.TransactionRepository;
import team16.bitcoinservice.service.TransactionService;

import java.text.MessageFormat;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Override
    public Transaction createTransaction(Merchant merchant, BitcoinPaymentDTO bitcoinPaymentDTO) {

       Transaction transaction = new Transaction();
       transaction.setMerchant(merchant);
       transaction.setOrderId(bitcoinPaymentDTO.getOrderId());
       transaction.setPriceCurrency(bitcoinPaymentDTO.getCurrency());
       transaction.setPriceAmount(bitcoinPaymentDTO.getAmount());
       transaction.setErrorUrl(bitcoinPaymentDTO.getErrorUrl());
       transaction.setFailedUrl(bitcoinPaymentDTO.getFailedUrl());
       transaction.setSuccessUrl(bitcoinPaymentDTO.getSuccessUrl());

       return this.transactionRepository.save(transaction);
    }

    @Override
    public boolean changeTransactionStatus(Long id, String status) {

        logger.info(MessageFormat.format("Changing transaction status | TransactionId: {0} | New status: {1}",
                id, status));
        Transaction transaction = this.transactionRepository.findById(id).orElse(null);

        if(transaction == null){
            logger.warn("Transaction not found | TransactionId: " + id);
            return false;
        }
        transaction.setStatus(TransactionStatus.valueOf(status));
        this.transactionRepository.save(transaction);

        logger.info(MessageFormat.format("Transaction status changed | TransactionId: {0} | New status: {1}",
                id, status));
        return true;
    }

    @Override
    public Transaction updateTransaction(Transaction transaction, PaymentResponseDTO paymentResponseDTO) {

        logger.info(MessageFormat.format("Updating transaction | TransactionId: {0} | Status: {1}", transaction.getId(),
                paymentResponseDTO.getStatus().toUpperCase()));

        transaction.setPaymentId(paymentResponseDTO.getId());
        transaction.setCreatedAt(paymentResponseDTO.getCreated_at());
        transaction.setStatus(TransactionStatus.valueOf(paymentResponseDTO.getStatus().toUpperCase()));
        System.out.println("Transaction receive amount: " + paymentResponseDTO.getReceive_amount());

        if(!paymentResponseDTO.getReceive_amount().equals("")){
            try{
                transaction.setReceiveAmount(Double.parseDouble(paymentResponseDTO.getReceive_amount()));
            }catch(NumberFormatException e){
                logger.error("Updating receive amount for transaction failed | TransactionId: " + transaction.getId());
                return null;
            }
        }
        transaction.setReceiveCurrency(paymentResponseDTO.getReceive_currency());

        return this.transactionRepository.save(transaction);
    }

    @Override
    public Transaction findTransactionById(Long id) {
        return this.transactionRepository.findById(id).orElse(null);
    }

    @Override
    public boolean updateTransactionFromCoinGate(Transaction transaction) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + " " + transaction.getMerchant().getToken());
        HttpEntity<PaymentRequestDTO> request = new HttpEntity<>(headers);
        ResponseEntity<PaymentResponseDTO> responseEntity = null;

        try{
            responseEntity = this.restTemplate.exchange("https://api-sandbox.coingate.com/v2/orders/" + transaction.getPaymentId(),
                    HttpMethod.GET, request, PaymentResponseDTO.class);
        }catch(Exception e){

            logger.error(MessageFormat.format("Communication with CoinGate api failed. Retrieving information for transaction | TransactionId: {0} | PaymentId: {1}",
                    transaction.getId(), transaction.getPaymentId()));
            return false;
        }

        PaymentResponseDTO response = responseEntity.getBody();
        transaction = this.updateTransaction(transaction, response);

        if(transaction == null){
            return false;
        }
        return true;
    }

    @Override
    @Scheduled(cron = "${transactions.cron}") //ovo se radi jer callback notifikacije ne rade za localhost
    public void updateUnfinishedTransactionsFromCoinGate() {

        //pronadju se u bazi transakcije koje su new, pending i confirming
        //proveri se za njih da li su mozda promenile status

        List<Transaction> transactions = this.transactionRepository.findUnfinishedTransactions();

        if(transactions.size() > 0){
            logger.info("Updating unfinished transaction from CoinGate started.");
        }

        for(Transaction t : transactions){

            this.updateTransactionFromCoinGate(t);
        }

        if(transactions.size() > 0){
            logger.info("Updating unfinished transaction from CoinGate finished.");
        }
    }


}
