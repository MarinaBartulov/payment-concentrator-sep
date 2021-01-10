package team16.paymentserviceprovider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team16.paymentserviceprovider.model.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    PaymentMethod findByName(String name);
    @Query(value = "select p from PaymentMethod p inner join p.apps a where p.name = ?1 and a.id = ?2")
    PaymentMethod findByPaymentMethodNameAndAppId(String paymentMethodName, Long appId);
}
