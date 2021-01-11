package team16.bankpaymentservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.bankpaymentservice.model.Bank;
import team16.bankpaymentservice.repository.BankRepository;
import team16.bankpaymentservice.service.BankService;

@Service
public class BankServiceImpl implements BankService {

    @Autowired
    private BankRepository bankRepository;

    @Override
    public Bank findById(Long id) {
        return bankRepository.getOne(id);
    }

    @Override
    public Bank findByBankCode(String bankCode) {
        return bankRepository.findByBankCode(bankCode);
    }
}
