package team16.paypalservice.converter;

import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Base64;

@Converter
public class SensitiveDataConverter implements AttributeConverter<String, String> {

    //IvParameterSpec class specifies an initialization vector (IV)
    //IvParameterSpec(byte[] iv) - creates an IvParameterSpec object using the bytes in iv as the IV
    //za svaki token se mora cuvati iv da bi se mogao dekriptovati


    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(String data) {

        byte[] ivParameterVector = new byte[16];
        new SecureRandom().nextBytes(ivParameterVector);
        String ivString = Base64.getEncoder().encodeToString(ivParameterVector);

        KeyStore keystore;
        ClassPathResource resource = new ClassPathResource("aes-keystore.jceks");
        InputStream keystoreStream = resource.getInputStream();
        Key key = null;

        try {
            keystore = KeyStore.getInstance("JCEKS");
            keystore.load(keystoreStream, "password".toCharArray());
            key = keystore.getKey("aeskey", "password".toCharArray());

        } catch (Exception e) {
            System.out.println("Reading key failed.");
        }

        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivParameterVector));
            return Base64.getEncoder().encodeToString(c.doFinal(data.getBytes())) + "|" + ivString;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @SneakyThrows
    @Override
    public String convertToEntityAttribute(String dataDB) {

        String ivString = dataDB.split("\\|")[1];
        byte[] ivParameterVector = Base64.getDecoder().decode(ivString);
        String encryptedData = dataDB.split("\\|")[0];

        KeyStore keystore;
        ClassPathResource resource = new ClassPathResource("aes-keystore.jceks");
        InputStream keystoreStream = resource.getInputStream();
        Key key = null;

        try {
            keystore = KeyStore.getInstance("JCEKS");
            keystore.load(keystoreStream, "password".toCharArray());
            key = keystore.getKey("aeskey", "password".toCharArray());

        } catch (Exception e) {
            System.out.println("Reading key failed.");
        }

        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivParameterVector));
            return new String(c.doFinal(Base64.getDecoder().decode(encryptedData)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
