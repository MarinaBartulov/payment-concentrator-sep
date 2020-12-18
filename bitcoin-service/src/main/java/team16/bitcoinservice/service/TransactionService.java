package team16.bitcoinservice.service;

import team16.bitcoinservice.dto.BitcoinPaymentDTO;
import team16.bitcoinservice.model.Merchant;
import team16.bitcoinservice.model.Transaction;

public interface TransactionService {

    Transaction createTransaction(Merchant merchant, BitcoinPaymentDTO bitcoinPaymentDTO);
}
