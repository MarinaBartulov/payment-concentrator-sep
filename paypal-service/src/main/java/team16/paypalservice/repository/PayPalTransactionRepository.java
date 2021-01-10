package team16.paypalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team16.paypalservice.model.PayPalSubscription;
import team16.paypalservice.model.PayPalTransaction;

import java.util.List;

@Repository
public interface PayPalTransactionRepository extends JpaRepository<PayPalTransaction, Long> {

    PayPalTransaction findByPaymentId(String paymentId);

    @Query(value = "SELECT * FROM pay_pal_transaction ppt WHERE ppt.status = 'INITIATED' or ppt.status = 'CREATED'", nativeQuery = true)
    List<PayPalTransaction> findAllUnfinished();
}
