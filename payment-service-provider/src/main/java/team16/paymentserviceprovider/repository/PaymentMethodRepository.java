package team16.paymentserviceprovider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team16.paymentserviceprovider.model.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    PaymentMethod findByName(String name);
}
