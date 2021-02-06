package team16.paymentserviceprovider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team16.paymentserviceprovider.model.Subscription;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query(value = "from Subscription s join s.merchant m where m.email = ?1 and s.id = ?2")
    Subscription findSubscriptionForMerchant(String merchantEmail, Long id);

    @Query(value = "from Subscription s where s.status = 'INITIATED'")
    List<Subscription> findInitiatedSubscriptions();

    @Query(value = "from Subscription s where s.status = 'CREATED'")
    List<Subscription> findCreatedSubscriptions();
}
