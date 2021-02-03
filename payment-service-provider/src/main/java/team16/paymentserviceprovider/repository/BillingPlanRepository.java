package team16.paymentserviceprovider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team16.paymentserviceprovider.model.BillingPlan;

import java.util.List;

@Repository
public interface BillingPlanRepository extends JpaRepository<BillingPlan, Long> {

    @Query(value = "select bp from BillingPlan bp where bp.merchant.id = ?1 or bp.isDefault = true")
    List<BillingPlan> findAllByMerchant(Long id);
}
