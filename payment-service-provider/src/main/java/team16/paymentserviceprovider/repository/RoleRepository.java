package team16.paymentserviceprovider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team16.paymentserviceprovider.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
