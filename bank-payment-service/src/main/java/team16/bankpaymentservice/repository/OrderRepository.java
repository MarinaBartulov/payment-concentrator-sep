package team16.bankpaymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team16.bankpaymentservice.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
