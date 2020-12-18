package team16.bankpaymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.bankpaymentservice.dto.PaymentRequestDTO;
import team16.bankpaymentservice.dto.PaymentResponseInfoDTO;
import team16.bankpaymentservice.model.Merchant;
import team16.bankpaymentservice.model.Payment;

@Service
public class PaymentService {

    @Autowired
    private MerchantServiceImpl merchantService;

    @Autowired
    private PaymentServiceImpl paymentService;

    public PaymentResponseInfoDTO generatePaymentInfo(PaymentRequestDTO dto) throws Exception {
        validateRequestData(dto);
        // nakon provere, ako je sve u redu, kreiraju se:
        // payment id i payment url - koji preusmerava kupca na sajt banke
        Payment payment = new Payment();
        payment.setPaymentUrl("https://localhost:3002/issuer");
        Payment newPayment = paymentService.create(payment);
        return new PaymentResponseInfoDTO(newPayment.getPaymentId(), newPayment.getPaymentUrl());
    }

    private void validateRequestData(PaymentRequestDTO dto) throws Exception {
        // provera ispravnosti zahteva
        // 1. provera poklapanja kredencijala merchant id i password
        // 2. provera validnosti ostalih informacija
        if(dto.getMerchantId().equals("") || dto.getMerchantId() == null) {
            throw new Exception("Invalid merchant info."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(merchantService.findByMerchantId(dto.getMerchantId()) == null) {
            throw new Exception("Nonexistent merchant.");
        }
        Merchant merchant = merchantService.findByMerchantId(dto.getMerchantId());
        if(dto.getMerchantPassword().equals("") || dto.getMerchantPassword() == null) {
            throw new Exception("Invalid merchant info."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(!merchant.getPassword().equals(dto.getMerchantPassword())) {
            throw new Exception("Invalid merchant info.");
        }
        if(dto.getAmount() < 0) {
            throw new Exception("Amount cannot be negative.");
        }
        if(dto.getMerchantOrderId().equals("") || dto.getMerchantOrderId() == null) {
            throw new Exception("Invalid merchant order id."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(dto.getMerchantTimestamp() == null) {
            throw new Exception("Invalid merchant timestamp."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(dto.getSuccessUrl().equals("") || dto.getSuccessUrl() == null) {
            throw new Exception("Invalid merchant order id."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(dto.getFailedUrl().equals("") || dto.getFailedUrl() == null) {
            throw new Exception("Invalid merchant order id."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(dto.getErrorUrl().equals("") || dto.getErrorUrl() == null) {
            throw new Exception("Invalid merchant order id."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
    }
}
