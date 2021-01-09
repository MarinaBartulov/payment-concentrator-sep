package team16.paymentserviceprovider.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team16.paymentserviceprovider.dto.MerchantInfoDTO;
import team16.paymentserviceprovider.dto.MerchantPCDTO;
import team16.paymentserviceprovider.model.App;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.model.Role;
import team16.paymentserviceprovider.repository.MerchantRepository;
import team16.paymentserviceprovider.service.AppService;
import team16.paymentserviceprovider.service.EmailService;
import team16.paymentserviceprovider.service.MerchantService;
import team16.paymentserviceprovider.service.RoleService;

import javax.mail.MessagingException;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
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
    private PasswordEncoder passwordEncoder;

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
}
