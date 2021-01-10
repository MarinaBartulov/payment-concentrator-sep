package team16.paypalservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PaypalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaypalServiceApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
