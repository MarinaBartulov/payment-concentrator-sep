package team16.pccservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.pccservice.dto.PCCRequestDTO;
import team16.pccservice.enums.Status;
import team16.pccservice.model.PaymentRequest;
import team16.pccservice.repository.PaymentRequestRepository;
import team16.pccservice.service.PaymentRequestService;

import java.time.LocalDateTime;

@Service
public class PaymentRequestServiceImpl implements PaymentRequestService {

    @Autowired
    private PaymentRequestRepository paymentRequestRepository;

    @Override
    public PaymentRequest create(PCCRequestDTO dto) {

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setCreateTime(LocalDateTime.now());
        paymentRequest.setAcquirerOrderID(dto.getAcquirerOrderId());
        paymentRequest.setAcquirerTimestamp(dto.getAcquirerTimestamp());
        paymentRequest.setMerchantOrderId(dto.getMerchantOrderId());
        paymentRequest.setPaymentId(dto.getPaymentId());
        paymentRequest.setStatus(Status.INITIATED);

        return paymentRequestRepository.save(paymentRequest);
    }

    @Override
    public PaymentRequest update(PaymentRequest paymentRequest) {
        return paymentRequestRepository.save(paymentRequest);
    }

    @Override
    public PaymentRequest findByAcquirerOrderId(Long id) {
        return paymentRequestRepository.findPaymentRequestByAcquirerOrderID(id);
    }
}
