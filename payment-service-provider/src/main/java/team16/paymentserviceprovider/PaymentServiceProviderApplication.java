package team16.paymentserviceprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Base64;

@SpringBootApplication
public class PaymentServiceProviderApplication {

    public static void main(String[] args) throws IOException {

        SpringApplication.run(PaymentServiceProviderApplication.class, args);

    }

}
