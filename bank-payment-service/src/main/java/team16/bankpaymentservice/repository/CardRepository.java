package team16.bankpaymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team16.bankpaymentservice.model.Card;
import team16.bankpaymentservice.model.Merchant;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    @Query(value = "select * from card c where c.pan = ?1", nativeQuery = true)
    Card findByPan(String pan);

    @Query(value = "select * from card c where c.card_holder_id = ?1", nativeQuery = true)
    Card findByCardHolderId(Long cardHolderId);
}
