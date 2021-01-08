package team16.paymentserviceprovider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.paymentserviceprovider.dto.MerchantPCDTO;
import team16.paymentserviceprovider.exceptions.InvalidDataException;
import team16.paymentserviceprovider.model.App;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.repository.MerchantRepository;
import team16.paymentserviceprovider.service.AppService;
import team16.paymentserviceprovider.service.EmailService;
import team16.paymentserviceprovider.service.MerchantService;

import javax.mail.MessagingException;
import java.util.List;
import java.util.UUID;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private AppService appService;
    @Autowired
    private EmailService emailService;

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
        return merchantRepository.findMerchantByMerchantEmail(email);
    }

    @Override
    public boolean registerNewMerchant(MerchantPCDTO merchantPCDTO) throws MessagingException, InterruptedException {

        App app = this.appService.findByAppId(merchantPCDTO.getAppId());
        Merchant newMerchant = new Merchant(merchantPCDTO, app);
        String password = UUID.randomUUID().toString();
        newMerchant.setPassword(password);

        String confirmationUrl =  "https://localhost:3001/login";
        String text = "Hello " + merchantPCDTO.getMerchantName() + ",\n\nThis is your password: " + password + "." +
                "\n You have to login on this link " + confirmationUrl + " to choose payment methods and finish your registration."
        + "\n\nBest regards,\nPayment Concentrator";

        String subject = "Payment Concentrator - Merchant registration.";
        emailService.sendEmail(merchantPCDTO.getMerchantEmail(), subject, text);
        this.merchantRepository.save(newMerchant);
        return true;
    }

}
