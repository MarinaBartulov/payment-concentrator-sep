package team16.paymentserviceprovider.service;

import team16.paymentserviceprovider.dto.TransactionDTO;
import team16.paymentserviceprovider.model.BankTransaction;

public interface TransactionService {

    BankTransaction create(TransactionDTO dto) throws Exception;
}
