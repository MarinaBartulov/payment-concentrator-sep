package team16.bankpaymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class BankPaymentServiceApplication {

    public static void main(String[] args) throws IOException {

        SpringApplication.run(BankPaymentServiceApplication.class, args);

//        byte[] ivParameterVector = new byte[16];
//        new SecureRandom().nextBytes(ivParameterVector);
//        String ivString = Base64.getEncoder().encodeToString(ivParameterVector);
//
//        KeyStore keystore;
//        ClassPathResource resource = new ClassPathResource("aes-keystore.jceks");
//        InputStream keystoreStream = resource.getInputStream();
//        Key key = null;
//
//        try {
//            keystore = KeyStore.getInstance("JCEKS");
//            keystore.load(keystoreStream, "password".toCharArray());
//            key = keystore.getKey("aeskey", "password".toCharArray());
//        } catch (Exception e) {
//            System.out.println("Reading key failed.");
//        }
//
//        String data = "111111111111111111111111111111";
//        try {
//            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            c.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivParameterVector));
//            System.out.println(Base64.getEncoder().encodeToString(c.doFinal(data.getBytes())) + "|" + ivString);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

//    @Bean
//    RestTemplate restTemplate(){
//        return new RestTemplate();
//    }

}
