package team16.pccservice.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.pccservice.model.Bank;
import team16.pccservice.repository.BankRepository;
import team16.pccservice.service.BankService;

@Service
public class BankServiceImpl implements BankService {

    @Autowired
    private BankRepository bankRepository;

    @Override
    public Bank findByCode(String code) {
        return bankRepository.findByBankCode(code);
    }
}
