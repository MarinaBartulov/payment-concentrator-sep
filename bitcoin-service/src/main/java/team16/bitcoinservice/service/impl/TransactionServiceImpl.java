package team16.bitcoinservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.bitcoinservice.dto.BitcoinPaymentDTO;
import team16.bitcoinservice.model.Merchant;
import team16.bitcoinservice.model.Transaction;
import team16.bitcoinservice.repository.TransactionRepository;
import team16.bitcoinservice.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Transaction createTransaction(Merchant merchant, BitcoinPaymentDTO bitcoinPaymentDTO) {

        Transaction transaction = new Transaction(bitcoinPaymentDTO.getOrderId(),
                bitcoinPaymentDTO.getPaymentCurrency(), bitcoinPaymentDTO.getPaymentAmount(),merchant);

        return this.transactionRepository.save(transaction);
    }
}
