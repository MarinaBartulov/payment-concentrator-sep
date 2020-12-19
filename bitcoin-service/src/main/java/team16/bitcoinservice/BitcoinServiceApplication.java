package team16.bitcoinservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class BitcoinServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BitcoinServiceApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate(){
         return new RestTemplate();
    }

}
