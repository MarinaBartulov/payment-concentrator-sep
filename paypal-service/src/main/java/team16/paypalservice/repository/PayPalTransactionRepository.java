package team16.paypalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team16.paypalservice.model.PayPalTransaction;

@Repository
public interface PayPalTransactionRepository extends JpaRepository<PayPalTransaction, Long> {

    PayPalTransaction findByPaymentId(String paymentId);

}
