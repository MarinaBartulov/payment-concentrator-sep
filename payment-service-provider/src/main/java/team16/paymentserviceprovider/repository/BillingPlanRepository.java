package team16.paymentserviceprovider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team16.paymentserviceprovider.model.BillingPlan;

@Repository
public interface BillingPlanRepository extends JpaRepository<BillingPlan, Long> {
}
