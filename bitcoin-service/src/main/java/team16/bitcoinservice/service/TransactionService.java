package team16.bitcoinservice.service;

import team16.bitcoinservice.dto.BitcoinPaymentDTO;
import team16.bitcoinservice.dto.PaymentResponseDTO;
import team16.bitcoinservice.model.Merchant;
import team16.bitcoinservice.model.Transaction;

public interface TransactionService {

    Transaction createTransaction(Merchant merchant, BitcoinPaymentDTO bitcoinPaymentDTO);
    boolean changeTransactionStatus(Long id, String status);
    Transaction updateTransaction(Transaction transaction, PaymentResponseDTO paymentResponseDTO);
    Transaction findTransactionById(Long id);
    boolean updateTransactionFromCoinGate(Transaction transaction);
    void updateUnfinishedTransactionsFromCoinGate();
    Transaction findTransactionByOrderId(Long orderId);

}
