package team16.pccservice.service;

import team16.pccservice.model.Bank;

public interface BankService {

    Bank findByCode(String code);
}
