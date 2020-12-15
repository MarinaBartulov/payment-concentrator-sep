package team16.bankpaymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team16.bankpaymentservice.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
}
