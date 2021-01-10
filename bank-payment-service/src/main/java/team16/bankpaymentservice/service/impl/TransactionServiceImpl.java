package team16.bankpaymentservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.bankpaymentservice.model.Transaction;
import team16.bankpaymentservice.repository.TransactionRepository;
import team16.bankpaymentservice.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

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
}
