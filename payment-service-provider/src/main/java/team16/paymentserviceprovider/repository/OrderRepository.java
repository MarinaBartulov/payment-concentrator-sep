package team16.paymentserviceprovider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team16.paymentserviceprovider.model.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "from Order o join o.merchant m where m.email = ?1 and o.merchantOrderId = ?2")
    Order findOrderForMerchant(String merchantEmail, Long orderId);

    @Query(value = "from Order o where o.orderStatus = 'CREATED'")
    List<Order> findAllUnfinishedOrders();
}
