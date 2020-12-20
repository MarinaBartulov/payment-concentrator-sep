package team16.bankpaymentservice.service;

import team16.bankpaymentservice.model.Bank;

public interface BankService {

    Bank findById(Long id);

    Bank findByBankCode(String bankCode);
}
