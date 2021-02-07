package team16.bankpaymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BankPaymentServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(BankPaymentServiceApplication.class, args);
    }

}
