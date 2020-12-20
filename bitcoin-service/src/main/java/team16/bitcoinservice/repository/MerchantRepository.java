package team16.bitcoinservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team16.bitcoinservice.model.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Merchant findByEmail(String email);
}
