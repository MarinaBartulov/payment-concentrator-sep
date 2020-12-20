package team16.bankpaymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team16.bankpaymentservice.model.CardOwner;
import team16.bankpaymentservice.model.Merchant;

@Repository
public interface CardOwnerRepository extends JpaRepository<CardOwner, Long> {

    @Query(value = "select * from card_owner m where m.type = 'Merchant' and m.merchant_id = ?1", nativeQuery = true)
    Merchant findByMerchantId(String merchantId);
}
