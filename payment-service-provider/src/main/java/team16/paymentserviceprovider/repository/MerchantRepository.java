package team16.paymentserviceprovider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team16.paymentserviceprovider.model.App;
import team16.paymentserviceprovider.model.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Merchant findMerchantByMerchantId(String merchantId);
    Merchant findMerchantByEmail(String email);
}
