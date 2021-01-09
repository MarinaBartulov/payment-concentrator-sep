package team16.paymentserviceprovider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team16.paymentserviceprovider.model.App;

public interface AppRepository extends JpaRepository<App, Long> {

    App findByOfficialEmail(String email);
    App findByAppId(String appId);
}
