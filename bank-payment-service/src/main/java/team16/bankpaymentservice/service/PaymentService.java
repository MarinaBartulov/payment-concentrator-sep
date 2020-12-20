package team16.bankpaymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public PaymentService() {
        validationService = new ValidationService();
    }

    public PaymentResponseInfoDTO generatePaymentInfo(PaymentRequestDTO dto) throws Exception {
        System.out.println("------------------------DTO from PSP to Bank for Payment and Transaction creation-------------------------------");
        validateRequestData(dto);

        Payment payment = new Payment();
        payment.setPaymentUrl("https://localhost:3002/issuer");

        Merchant merchant = cardOwnerService.findByMerchantId(dto.getMerchantId());
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

        return new PaymentResponseInfoDTO(newPayment.getPaymentId(),
                newPayment.getPaymentUrl() + "/" + newPayment.getPaymentId());
    }

    private void validateRequestData(PaymentRequestDTO dto) throws InvalidDataException {
        System.out.println("------------------------DTO from PSP to Bank validation-------------------------------");
        if(!validationService.validateString(dto.getMerchantId()) ||
                !validationService.validateString(dto.getMerchantPassword())) {
            System.out.println("Id, pass null or empty");
            throw new InvalidDataException("Invalid merchant info.");
        }
        if(cardOwnerService.findByMerchantId(dto.getMerchantId()) == null) {
            System.out.println("nonexistent merchant");
            throw new InvalidDataException("Nonexistent merchant.");
        }

        Merchant merchant = cardOwnerService.findByMerchantId(dto.getMerchantId());
        if(!merchant.getPassword().equals(dto.getMerchantPassword())) {
            System.out.println("passwords dont match");
            throw new InvalidDataException("Invalid merchant info.");
        }
        if(dto.getAmount() < 0) {
            System.out.println("negative amount");
            throw new InvalidDataException("Amount cannot be negative.");
        }
        if(dto.getMerchantOrderId() == null) {
            System.out.println("merchant order id null");
            throw new InvalidDataException("Invalid merchant order id."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(dto.getMerchantTimestamp() == null) {
            System.out.println("merchant timestamp null");
            throw new InvalidDataException("Invalid merchant timestamp."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(!validationService.validateString(dto.getSuccessUrl()) ||
                !validationService.validateString(dto.getFailedUrl()) ||
                !validationService.validateString(dto.getErrorUrl())) {
            System.out.println("invalid urls - null or empty");
            throw new InvalidDataException("Invalid URLs.");
        }
    }
}
