package team16.paypalservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PaypalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaypalServiceApplication.class, args);
    }

}
