package team16.bankpaymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

import org.springframework.core.io.ClassPathResource;

@SpringBootApplication
public class BankPaymentServiceApplication {

    public static void main(String[] args) throws IOException {

        SpringApplication.run(BankPaymentServiceApplication.class, args);
    }

}
