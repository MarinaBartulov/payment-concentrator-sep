package team16.bankpaymentservice.service;

import team16.bankpaymentservice.model.Transaction;

public interface TransactionService {

    Transaction findById(Long id);

    Transaction findByAcquirerOrderId(Long id);

    Transaction create(Transaction transaction);

    Transaction update(Transaction transaction);

    Transaction findByMerchantOrderId(Long id);
}
