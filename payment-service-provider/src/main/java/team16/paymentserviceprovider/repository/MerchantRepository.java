package team16.paymentserviceprovider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team16.paymentserviceprovider.model.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    @Query(value = "select * from merchant m where m.merchant_id = ?1", nativeQuery = true)
    Merchant findByMerchantId(String merchantId);
}
