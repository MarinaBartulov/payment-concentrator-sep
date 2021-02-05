package team16.bankpaymentservice.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.bankpaymentservice.dto.ClientAuthDTO;
import team16.bankpaymentservice.dto.FormFieldDTO;
import team16.bankpaymentservice.dto.FormFieldType;
import team16.bankpaymentservice.dto.MerchantCardInfoDTO;
import team16.bankpaymentservice.exceptions.InvalidDataException;
import team16.bankpaymentservice.model.Bank;
import team16.bankpaymentservice.model.Card;
import team16.bankpaymentservice.model.Merchant;
import team16.bankpaymentservice.repository.MerchantRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CardService cardService;

    @Autowired
    private BankService bankService;

    private ValidationService validationService;

    public MerchantServiceImpl() { validationService = new ValidationService(); }

    @Override
    public Merchant findById(Long id) {
        return merchantRepository.findById(id).orElseGet(null);
    }

    @Override
    public Merchant findByEmail(String email) {
        return this.merchantRepository.findByMerchantEmail(email);
    }

    @Override
    public List<FormFieldDTO> getFormFields() {

        List<FormFieldDTO> formFields = new ArrayList<>();
        formFields.add(new FormFieldDTO("pan", "PAN", FormFieldType.text, true));
//        formFields.add(new FormFieldDTO("merchantId", "Merchant Id", FormFieldType.text, true));
//        formFields.add(new FormFieldDTO("password", "Merchant Password", FormFieldType.password, true));
        return formFields;
    }

    @Override
    public Merchant addNewMerchant(String merchantData, String email) {
        Gson gson = new Gson();
        try {
            Merchant merchant = gson.fromJson(merchantData, Merchant.class);
            merchant.setMerchantEmail(email);
            Merchant newMerchant = this.merchantRepository.save(merchant);

            // sending email to this merchant - to get card information
            String gettingCardInfoUrl = "https://localhost:3002/card-info/" + newMerchant.getId();
            String text = "Dear Sir or Madam,  " + ",\n\nPlease, fill in your bank card information by visiting this link: " + gettingCardInfoUrl
                    + "\n\nBest regards,\nYour Bank";

            String subject = "Bank - card authentication";
            emailService.sendEmail(merchant.getMerchantEmail(), subject, text);

            return newMerchant;
        }catch(Exception e){
            return null;
        }
    }

    public Card merchantCardAuth(MerchantCardInfoDTO dto, Long merchantId) throws Exception {
        try {
            validateMerchantCardInput(dto);

            Card card = cardService.findByPan(dto.getPan());

            Merchant merchant = findById(merchantId);
            if(merchant == null) {
                throw new Exception("Nonexistent merchant");
            }

            merchant.setCard(card);

            Bank bank = bankService.findByBankCode(card.getPAN().substring(0, 3));
            if(bank == null) {
                throw new Exception("Nonexistent bank");
            }

            merchant.setBank(bank);

            merchantRepository.save(merchant);

            return card;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Merchant save(Merchant merchant) {
        return merchantRepository.save(merchant);
    }

    private void validateMerchantCardInput(MerchantCardInfoDTO dto) throws Exception {
        if (!validationService.validateString(dto.getPan()) ||
                !validationService.validateString(dto.getSecurityNumber())) {
            throw new InvalidDataException("Merchant Card information empty");
        }
        Card card = cardService.findByPan(dto.getPan());
        if (!card.getSecurityCode().equals(dto.getSecurityNumber())) {
            throw new InvalidDataException("Invalid security number");
        }
        if (!validationService.convertToYearMonthFormat(dto.getExpirationDate()).equals(card.getExpirationDate())) {
            throw new InvalidDataException("False expiration date");
        }
    }
}
