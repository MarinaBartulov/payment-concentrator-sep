package team16.bankpaymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team16.bankpaymentservice.model.Merchant;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Merchant findByMerchantEmail(String email);
}
