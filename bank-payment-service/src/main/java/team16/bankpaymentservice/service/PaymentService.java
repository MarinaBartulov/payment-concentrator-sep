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

        validateRequestData(dto);

        Payment payment = new Payment();
        payment.setPaymentUrl("https://localhost:3002/issuer");

        Merchant merchant = cardOwnerService.findByMerchantId(dto.getMerchantId());

        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setMerchant(merchant);
        transaction.setMerchantOrderId(dto.getMerchantOrderId());
        transaction.setMerchantTimestamp(dto.getMerchantTimestamp());
        transaction.setStatus(TransactionStatus.INITIATED);
        Transaction newTransaction = transactionService.create(transaction);

        payment.setTransaction(newTransaction);

        Payment newPayment = paymentService.create(payment);

        return new PaymentResponseInfoDTO(newPayment.getPaymentId(),
                newPayment.getPaymentUrl() + "/" + newPayment.getPaymentId());
    }

    private void validateRequestData(PaymentRequestDTO dto) throws InvalidDataException {
        if(!validationService.validateString(dto.getMerchantId()) ||
                !validationService.validateString(dto.getMerchantPassword())) {
            throw new InvalidDataException("Invalid merchant info.");
        }
        if(cardOwnerService.findByMerchantId(dto.getMerchantId()) == null) {
            throw new InvalidDataException("Nonexistent merchant.");
        }
        Merchant merchant = cardOwnerService.findByMerchantId(dto.getMerchantId());
        if(!merchant.getPassword().equals(dto.getMerchantPassword())) {
            throw new InvalidDataException("Invalid merchant info.");
        }
        if(dto.getAmount() < 0) {
            throw new InvalidDataException("Amount cannot be negative.");
        }
        if(dto.getMerchantOrderId() == null) {
            throw new InvalidDataException("Invalid merchant order id."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(dto.getMerchantTimestamp() == null) {
            throw new InvalidDataException("Invalid merchant timestamp."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(!validationService.validateString(dto.getSuccessUrl()) ||
                !validationService.validateString(dto.getFailedUrl()) ||
                !validationService.validateString(dto.getErrorUrl())) {
            throw new InvalidDataException("Invalid URLs.");
        }
    }
}
