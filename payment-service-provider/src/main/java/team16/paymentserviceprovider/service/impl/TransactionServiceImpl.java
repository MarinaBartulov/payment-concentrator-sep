package team16.paymentserviceprovider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.paymentserviceprovider.dto.TransactionDTO;
import team16.paymentserviceprovider.model.BankTransaction;
import team16.paymentserviceprovider.model.Order;
import team16.paymentserviceprovider.repository.BankTransactionRepository;
import team16.paymentserviceprovider.service.OrderService;
import team16.paymentserviceprovider.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private BankTransactionRepository bankTransactionRepository;

    @Autowired
    private OrderService orderService;

    @Override
    public BankTransaction create(TransactionDTO dto) throws Exception {

        Order order = orderService.findById(dto.getMerchantOrderId());

        if(order == null) {
            throw new Exception("Nonexistent order");
        }

        BankTransaction transaction = new BankTransaction();
        transaction.setPaymentId(dto.getPaymentId());
        transaction.setMerchantOrderId(dto.getMerchantOrderId());
        transaction.setAcquirerOrderId(dto.getAcquirerOrderId());
        transaction.setAcquirerTimestamp(dto.getAcquirerTimestamp());
        transaction.setIssuerOrderId(dto.getIssuerOrderId());
        transaction.setIssuerTimestamp(dto.getIssuerTimestamp());
        transaction.setStatus(dto.getStatus());
        transaction.setOrder(order);

        return bankTransactionRepository.save(transaction);
    }
}
