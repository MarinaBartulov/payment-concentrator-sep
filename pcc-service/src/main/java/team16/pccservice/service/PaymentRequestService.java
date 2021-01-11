package team16.pccservice.service;

import team16.pccservice.dto.PCCRequestDTO;
import team16.pccservice.model.PaymentRequest;

public interface PaymentRequestService {

    PaymentRequest create(PCCRequestDTO dto);
    PaymentRequest update(PaymentRequest paymentRequest);
    PaymentRequest findByAcquirerOrderId(Long id);
}
