package team16.bankpaymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team16.bankpaymentservice.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "select * from payment p where p.transaction_id = ?1", nativeQuery = true)
    Payment findPaymentByTransactionId(Long transactionId);
}
