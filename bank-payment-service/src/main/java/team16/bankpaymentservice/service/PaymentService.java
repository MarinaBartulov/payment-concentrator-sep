package team16.bankpaymentservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.bankpaymentservice.controller.CardController;
import team16.bankpaymentservice.dto.PaymentRequestDTO;
import team16.bankpaymentservice.dto.PaymentResponseInfoDTO;
import team16.bankpaymentservice.enums.TransactionStatus;
import team16.bankpaymentservice.exceptions.InvalidDataException;
import team16.bankpaymentservice.model.Merchant;
import team16.bankpaymentservice.model.Payment;
import team16.bankpaymentservice.model.Transaction;

@Service
public class PaymentService {

    @Autowired
    private CardOwnerServiceImpl cardOwnerService;

    @Autowired
    private PaymentServiceImpl paymentService;

    @Autowired
    private TransactionServiceImpl transactionService;

    private ValidationService validationService;

    Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public PaymentService() {
        validationService = new ValidationService();
    }

    public PaymentResponseInfoDTO generatePaymentInfo(PaymentRequestDTO dto) throws Exception {
        System.out.println("------------------------DTO from PSP to Bank for Payment and Transaction creation-------------------------------");
        validateRequestData(dto);

        Payment payment = new Payment();
        payment.setPaymentUrl("https://localhost:3002/issuer");

        Merchant merchant = cardOwnerService.findByMerchantEmail(dto.getMerchantEmail());
        System.out.println("Merchant id: " + merchant.getMerchantId());

        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setMerchant(merchant);
        transaction.setMerchantOrderId(dto.getMerchantOrderId());
        transaction.setMerchantTimestamp(dto.getMerchantTimestamp());
        transaction.setStatus(TransactionStatus.INITIATED);
        Transaction newTransaction = transactionService.create(transaction);
        System.out.println("Transaction: ");
        System.out.println(newTransaction.getId());
        System.out.println(newTransaction.getMerchant().getMerchantId());
        System.out.println(newTransaction.getAmount());
        System.out.println(newTransaction.getMerchantOrderId());
        System.out.println(newTransaction.getMerchantTimestamp());
        System.out.println(newTransaction.getStatus());

        payment.setTransaction(newTransaction);

        Payment newPayment = paymentService.create(payment);
        System.out.println("Payment: ");
        System.out.println(newPayment.getPaymentId());
        System.out.println(newPayment.getPaymentUrl());
        System.out.println(newPayment.getTransaction().getId());

        logger.info("New payment and transaction created. Sending redirection URL");

        return new PaymentResponseInfoDTO(newPayment.getPaymentId(),
                newPayment.getPaymentUrl() + "/" + newPayment.getPaymentId());
    }

    private void validateRequestData(PaymentRequestDTO dto) throws InvalidDataException {
        System.out.println("------------------------DTO from PSP to Bank validation-------------------------------");
        if(!validationService.validateString(dto.getMerchantId()) ||
                !validationService.validateString(dto.getMerchantEmail()) ||
                !validationService.validateString(dto.getMerchantPassword())) {
            System.out.println("Id, pass null or empty");
            logger.error("Invalid merchant info. Id, pass null or empty");
            throw new InvalidDataException("Invalid merchant info.");
        }
        if(cardOwnerService.findByMerchantEmail(dto.getMerchantEmail()) == null) {
            System.out.println("nonexistent merchant");
            logger.error("Nonexistent merchant");
            throw new InvalidDataException("Nonexistent merchant.");
        }

        Merchant merchant = cardOwnerService.findByMerchantEmail(dto.getMerchantEmail());
        if(!merchant.getMerchantId().equals(dto.getMerchantId())) {
            System.out.println("merchant ids dont match");
            logger.error("Invalid merchant info. Merchant ids don't match");
            throw new InvalidDataException("Invalid merchant info.");
        }
        if(!merchant.getPassword().equals(dto.getMerchantPassword())) {
            System.out.println("passwords dont match");
            logger.error("Invalid merchant info. Passwords don't match");
            throw new InvalidDataException("Invalid merchant info.");
        }
        if(dto.getAmount() < 0) {
            System.out.println("negative amount");
            logger.error("Amount cannot be negative");
            throw new InvalidDataException("Amount cannot be negative.");
        }
        if(dto.getMerchantOrderId() == null) {
            System.out.println("merchant order id null");
            logger.error("Invalid merchant order id");
            throw new InvalidDataException("Invalid merchant order id."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(dto.getMerchantTimestamp() == null) {
            System.out.println("merchant timestamp null");
            logger.error("Invalid merchant timestamp");
            throw new InvalidDataException("Invalid merchant timestamp."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(!validationService.validateString(dto.getSuccessUrl()) ||
                !validationService.validateString(dto.getFailedUrl()) ||
                !validationService.validateString(dto.getErrorUrl())) {
            System.out.println("invalid urls - null or empty");
            logger.error("Invalid URLs");
            throw new InvalidDataException("Invalid URLs.");
        }
    }
}
