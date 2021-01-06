package team16.paypalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team16.paypalservice.model.PayPalSubscription;

@Repository
public interface PayPalSubscriptionRepository extends JpaRepository<PayPalSubscription, Long> {
}
