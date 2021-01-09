package team16.pccservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team16.pccservice.model.PaymentRequest;

public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, Long> {

    PaymentRequest findPaymentRequestByAcquirerOrderID(Long acquirerOrderId);
}
