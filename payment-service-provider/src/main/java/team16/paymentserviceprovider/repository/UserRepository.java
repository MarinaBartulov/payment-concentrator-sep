package team16.paymentserviceprovider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team16.paymentserviceprovider.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
