package team16.bankpaymentservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.bankpaymentservice.dto.PaymentRequestDTO;
import team16.bankpaymentservice.dto.PaymentResponseInfoDTO;
import team16.bankpaymentservice.enums.TransactionStatus;
import team16.bankpaymentservice.exceptions.InvalidDataException;
import team16.bankpaymentservice.model.Merchant;
import team16.bankpaymentservice.model.Payment;
import team16.bankpaymentservice.model.Transaction;
import team16.bankpaymentservice.service.impl.CardOwnerServiceImpl;
import team16.bankpaymentservice.service.impl.TransactionServiceImpl;

@Service
public class PaymentService {

    @Autowired
    private CardOwnerServiceImpl cardOwnerService;

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private TransactionServiceImpl transactionService;

    private ValidationService validationService;

    Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public PaymentService() {
        validationService = new ValidationService();
    }

    public PaymentResponseInfoDTO generatePaymentInfo(PaymentRequestDTO dto) throws Exception {

        validateRequestData(dto);

        Payment payment = new Payment();
        payment.setPaymentUrl("https://localhost:3002/issuer");

        Merchant merchant = cardOwnerService.findByMerchantEmail(dto.getMerchantEmail());

        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setMerchant(merchant);
        transaction.setMerchantOrderId(dto.getMerchantOrderId());
        transaction.setMerchantTimestamp(dto.getMerchantTimestamp());
        transaction.setStatus(TransactionStatus.INITIATED);
        Transaction newTransaction = transactionService.create(transaction);

        payment.setTransaction(newTransaction);

        Payment newPayment = paymentService.update(payment);

        logger.info("New payment and transaction created. Sending redirection URL");

        return new PaymentResponseInfoDTO(newPayment.getPaymentId(),
                newPayment.getPaymentUrl() + "/" + newPayment.getPaymentId());
    }

    private void validateRequestData(PaymentRequestDTO dto) throws InvalidDataException {
        if(!validationService.validateString(dto.getMerchantId()) ||
                !validationService.validateString(dto.getMerchantEmail()) ||
                !validationService.validateString(dto.getMerchantPassword())) {
            logger.error("Invalid merchant info. Id, pass null or empty");
            throw new InvalidDataException("Invalid merchant info.");
        }
        if(cardOwnerService.findByMerchantEmail(dto.getMerchantEmail()) == null) {
            logger.error("Nonexistent merchant");
            throw new InvalidDataException("Nonexistent merchant.");
        }

        Merchant merchant = cardOwnerService.findByMerchantEmail(dto.getMerchantEmail());
        if(!merchant.getMerchantId().equals(dto.getMerchantId())) {
            logger.error("Invalid merchant info. Merchant ids don't match");
            throw new InvalidDataException("Invalid merchant info.");
        }
        if(!merchant.getPassword().equals(dto.getMerchantPassword())) {
            logger.error("Invalid merchant info. Passwords don't match");
            throw new InvalidDataException("Invalid merchant info.");
        }
        if(dto.getAmount() < 0) {
            logger.error("Amount cannot be negative");
            throw new InvalidDataException("Amount cannot be negative.");
        }
        if(dto.getMerchantOrderId() == null) {
            logger.error("Invalid merchant order id");
            throw new InvalidDataException("Invalid merchant order id."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(dto.getMerchantTimestamp() == null) {
            logger.error("Invalid merchant timestamp");
            throw new InvalidDataException("Invalid merchant timestamp."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
    }
}
