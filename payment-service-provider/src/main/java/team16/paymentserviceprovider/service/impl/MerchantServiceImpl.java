package team16.paymentserviceprovider.service.impl;

import com.google.gson.Gson;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import team16.paymentserviceprovider.dto.*;
import team16.paymentserviceprovider.model.App;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.model.PaymentMethod;
import team16.paymentserviceprovider.model.Role;
import team16.paymentserviceprovider.repository.MerchantRepository;
import team16.paymentserviceprovider.service.AppService;
import team16.paymentserviceprovider.service.MerchantService;
import team16.paymentserviceprovider.service.PaymentMethodService;
import team16.paymentserviceprovider.service.RoleService;

import javax.mail.MessagingException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private AppService appService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PaymentMethodService paymentMethodService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(MerchantServiceImpl.class);

    @Override
    public Merchant findOne(Long id) {
        return merchantRepository.findById(id).orElseGet(null);
    }

    @Override
    public List<Merchant> findAll() {
        return merchantRepository.findAll();
    }

    @Override
    public Merchant findByMerchantId(String id) {
        return merchantRepository.findMerchantByMerchantId(id);
    }

    @Override
    public Merchant findByMerchantEmail(String email) {
        return merchantRepository.findMerchantByEmail(email);
    }

    @Override
    public boolean registerNewMerchant(MerchantPCDTO merchantPCDTO) throws MessagingException, InterruptedException {

        App app = this.appService.findByAppId(merchantPCDTO.getAppId());
        Merchant newMerchant = new Merchant(merchantPCDTO, app);
        Role role = this.roleService.findByName("ROLE_MERCHANT");
        newMerchant.getRoles().add(role);
        newMerchant.setEnabled(true);
        String password = this.generateCommonLangPassword();
        newMerchant.setPassword(passwordEncoder.encode(password));

        String confirmationUrl = "https://localhost:3001/login";
        String text = "Hello " + merchantPCDTO.getMerchantName() + ",\n\nThis is your password: " + password +
                "\n You have to login on this link " + confirmationUrl + " to choose payment methods and finish your registration."
                + "\n\nBest regards,\nPayment Concentrator";

        String subject = "Payment Concentrator - Merchant registration.";
        emailService.sendEmail(merchantPCDTO.getMerchantEmail(), subject, text);
        this.merchantRepository.save(newMerchant);
        return true;
    }

    @Override
    public MerchantInfoDTO getMyInfo(Authentication currentUser) {
        String email = currentUser.getName();
        Merchant m = this.findByMerchantEmail(email);
        return new MerchantInfoDTO(m.isPasswordChanged(), m.isPmChosen());
    }

    @Override
    public Merchant save(Merchant merchant) {
        return this.merchantRepository.save(merchant);
    }

    @Override
    public String addPaymentMethodForCurrentMerchant(String authToken, String paymentMethodName, Map<String, Object> formValues) {

        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        String email = currentUser.getName();
        Merchant merchant = this.findByMerchantEmail(email);

        if(merchant == null){
            logger.warn("Merchant with email " + email + " doesn't exist.");
            return "Merchant does not exist.";
        }

        PaymentMethod pm = this.paymentMethodService.findByPaymentMethodNameAndApp(paymentMethodName, merchant.getApp().getId());

        if(pm == null){
            logger.warn("Payment method not supported. Merchant's email: " + email + ", payment method: " + paymentMethodName);
            return "Merchant's application does not support this payment method.";
        }

        Gson gsonObj = new Gson();
        String jsonString = gsonObj.toJson(formValues);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        try {
            restTemplate.postForEntity("https://localhost:8083/" + paymentMethodName.toLowerCase() + "-payment-service/api/merchant", request, ResponseEntity.class);
        }
        catch(Exception e) {
            e.printStackTrace();
            logger.error("Error while adding merchant data on the payment service. Merchant: " + email + ", payment method: " + paymentMethodName);
            return "Error occurred while adding merchant data on the payment service";
        }

        if(!merchant.isPmChosen()) {
            merchant.setPmChosen(true);
            try {
                HttpEntity<MerchantActivationDTO> activationRequest = new HttpEntity<>(new MerchantActivationDTO(merchant.getEmail()));
                restTemplate.exchange(merchant.getActivationUrl(), HttpMethod.PUT, activationRequest, ResponseEntity.class);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Error occurred while activating merchant on his app. Merchant: " + merchant.getEmail());
                return "Error occurred while activating merchant on his app.";
            }
        }
        merchant.getPaymentMethods().add(pm);

        //******* OVAJ DEO PROBLEMATICAN *******
        if(paymentMethodName.equals("Bank")){
            merchant.setMerchantId((String) formValues.get("merchantId"));
            merchant.setMerchantPassword((String) formValues.get("password"));
        }
        this.merchantRepository.save(merchant);
        return null;
    }

    private String generateCommonLangPassword() {
        String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(2);
        String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
        String totalChars = RandomStringUtils.randomAlphanumeric(2);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
                .concat(numbers)
                .concat(specialChar)
                .concat(totalChars);
        List<Character> pwdChars = combinedChars.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        String password = pwdChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        return password;
    }

    @Override
    @Transactional
    @Rollback(false)
    public MerchantBankDTO updateMerchant(String email, MerchantBankDTO dto) throws Exception {
        Merchant merchant = findByMerchantEmail(email);
        if(merchant == null) {
            throw new Exception("Merchant with given email doesn't exist.");
        }

        merchant.setMerchantId(dto.getMerchantId());
        merchant.setMerchantPassword(dto.getMerchantPassword());
        Merchant saved = save(merchant);

        MerchantBankDTO newDTO = new MerchantBankDTO(saved.getEmail(), saved.getMerchantId(), saved.getMerchantPassword());

        return newDTO;
    }

    @Override
    public MerchantURLsDTO getMerchantURLs(String email) {
        Merchant merchant = findByMerchantEmail(email);
        MerchantURLsDTO merchantURLsDTO = new MerchantURLsDTO(merchant.getMerchantSuccessUrl(),
                merchant.getMerchantErrorUrl(), merchant.getMerchantFailedUrl());
        return merchantURLsDTO;
    }
}
