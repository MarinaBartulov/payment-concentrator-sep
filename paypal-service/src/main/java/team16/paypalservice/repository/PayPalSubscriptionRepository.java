package team16.paypalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team16.paypalservice.model.PayPalSubscription;

import java.util.List;

@Repository
public interface PayPalSubscriptionRepository extends JpaRepository<PayPalSubscription, Long> {

    @Query(value = "SELECT * FROM pay_pal_subscription pps WHERE pps.status = 'INITIATED' or pps.status = 'CREATED'", nativeQuery = true)
    List<PayPalSubscription> findAllUnfinished();
}
