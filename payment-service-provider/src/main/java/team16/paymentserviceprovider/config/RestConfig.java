package team16.paymentserviceprovider.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

    private static final String URL_FORMAT = "%s://%s:%s";

    @Value("${PROTOCOL:http}")
    private String protocol;

    @Value("${DOMAIN:localhost}")
    private String domain;

    @Value("${PORT:8083}")  // svaki zahtev bi trebalo da se salje posredstvom zuul-a
    private String port;

    public String url() {
        return String.format(URL_FORMAT, protocol, domain, port);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
