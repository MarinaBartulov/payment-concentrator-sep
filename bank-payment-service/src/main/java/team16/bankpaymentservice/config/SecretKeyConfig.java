//package team16.bankpaymentservice.config;
//
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
//
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import javax.crypto.Cipher;
//import javax.crypto.spec.SecretKeySpec;
//import java.security.NoSuchAlgorithmException;
//import java.util.Base64;
//
//public class SecretKeyConfig {
//
//    // create new key
//    SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
//    // get base64 encoded version of the key
//    String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
//
//    // decode the base64 encoded string
//    byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
//    // rebuild key using SecretKeySpec
//    SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
//
//    // SEP
//    public static SecretKey API_SECRET_KEY;
//
//    static {
//        try {
//            API_SECRET_KEY = KeyGenerator.getInstance("AES").generateKey();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//    }
//
//    ;
//    public static String API_SALT = "3203E18B168E35DF";
//    public static String API_IV = "80EDF0003F255A6E520DAAA59EF4DE13";
//
//    public SecretKeyConfig() throws NoSuchAlgorithmException {
//    }
//
//    @EventListener(ApplicationReadyEvent.class)
//
//    public static byte[] encryptText(String plainText,SecretKey secKey) throws Exception{
//        // AES defaults to AES/ECB/PKCS5Padding in Java 7
//        Cipher aesCipher = Cipher.getInstance("AES");
//        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
//        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
//        return byteCipherText;
//    }
//
//    public static String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {
//        // AES defaults to AES/ECB/PKCS5Padding in Java 7
//        Cipher aesCipher = Cipher.getInstance("AES");
//        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
//        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
//        return new String(bytePlainText);
//    }
//}
