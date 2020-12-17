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
        payment.setPayment_url("https://localhost:3002/issuer");
        Payment newPayment = paymentService.create(payment);
        return new PaymentResponseInfoDTO(newPayment.getPayment_id(), newPayment.getPayment_url());
    }

    private void validateRequestData(PaymentRequestDTO dto) throws Exception {
        // provera ispravnosti zahteva
        // 1. provera poklapanja kredencijala merchant id i password
        // 2. provera validnosti ostalih informacija
        if(dto.getMerchant_id().equals("") || dto.getMerchant_id() == null) {
            throw new Exception("Invalid merchant info."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(merchantService.findByMerchantId(dto.getMerchant_id()) == null) {
            throw new Exception("Nonexistent merchant.");
        }
        Merchant merchant = merchantService.findByMerchantId(dto.getMerchant_id());
        if(dto.getMerchant_password().equals("") || dto.getMerchant_password() == null) {
            throw new Exception("Invalid merchant info."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(!merchant.getPassword().equals(dto.getMerchant_password())) {
            throw new Exception("Invalid merchant info.");
        }
        if(dto.getAmount() < 0) {
            throw new Exception("Amount cannot be negative.");
        }
        if(dto.getMerchant_order_id().equals("") || dto.getMerchant_order_id() == null) {
            throw new Exception("Invalid merchant order id."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(dto.getMerchant_timestamp() == null) {
            throw new Exception("Invalid merchant timestamp."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(dto.getSuccess_url().equals("") || dto.getSuccess_url() == null) {
            throw new Exception("Invalid merchant order id."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(dto.getFailed_url().equals("") || dto.getFailed_url() == null) {
            throw new Exception("Invalid merchant order id."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
        if(dto.getError_url().equals("") || dto.getError_url() == null) {
            throw new Exception("Invalid merchant order id."); // validacione poruke genericke, da ne iskazuju sta tacno ne valja
        }
    }
}
