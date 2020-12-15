package team16.paymentserviceprovider.config;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.apache.http.client.HttpClient;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.security.KeyStore;

@Configuration
public class RestConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {


        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(
                httpClient("PKCS12", "psp-service.p12", "password", "psp-service",
                        "PKCS12", "truststore.p12", "password")));
    }


    @Bean
    public HttpClient httpClient(String keystoreType, String keystore, String keystorePassword, String alias,
                                 String truststoreType, String truststore, String truststorePassword) {
        try {

            ClassPathResource resource1 = new ClassPathResource("psp-service.p12");
            ClassPathResource resource2 = new ClassPathResource("truststore.p12");

            KeyStore keyStore = KeyStore.getInstance(keystoreType);
            keyStore.load(new FileInputStream(resource1.getFile()), keystorePassword.toCharArray());
            KeyStore trustStore = KeyStore.getInstance(truststoreType);
            trustStore.load(new FileInputStream(resource2.getFile()), truststorePassword.toCharArray());

            SSLContext sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(trustStore, null)
                    .loadKeyMaterial(keyStore, keystorePassword.toCharArray(), (aliases, socket) -> alias)
                    .build();

            SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslcontext,
                    new String[]{"TLSv1.2"},
                    null, (hostname, sslSession) -> true);

            return HttpClients.custom().setSSLSocketFactory(sslFactory).build();
        } catch (Exception e) {
            throw new IllegalStateException("Error while configuring rest template", e);
        }
    }
}
