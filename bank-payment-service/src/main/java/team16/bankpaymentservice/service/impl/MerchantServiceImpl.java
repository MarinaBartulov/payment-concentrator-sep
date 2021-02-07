package team16.bankpaymentservice.service.impl;

import com.google.gson.Gson;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.bankpaymentservice.dto.FormFieldDTO;
import team16.bankpaymentservice.dto.FormFieldType;
import team16.bankpaymentservice.dto.PanDTO;
import team16.bankpaymentservice.model.Bank;
import team16.bankpaymentservice.model.Card;
import team16.bankpaymentservice.model.CardOwner;
import team16.bankpaymentservice.model.Merchant;
import team16.bankpaymentservice.repository.MerchantRepository;
import team16.bankpaymentservice.service.BankService;
import team16.bankpaymentservice.service.CardOwnerService;
import team16.bankpaymentservice.service.CardService;
import team16.bankpaymentservice.service.MerchantService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private CardService cardService;

    @Autowired
    private BankService bankService;

    @Autowired
    private CardOwnerService cardOwnerService;

    Logger logger = LoggerFactory.getLogger(MerchantServiceImpl.class);

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
        return formFields;
    }

    @Override
    public Merchant addNewMerchant(String merchantData, String email) throws Exception {

        // kao merchant data stize pan
        Gson gson = new Gson();
        PanDTO pan = null;
        try {
            pan = gson.fromJson(merchantData, PanDTO.class);
        } catch(Exception e){
            logger.error("Cannot register merchant - invalid PAN.");
            throw new Exception("Invalid PAN.");
        }

        Card card = cardService.findByPan(pan.getPan());
        if(card == null) {
            logger.error("Cannot register merchant - Card with given PAN doesn't exist.");
            throw new Exception("Card with this PAN doesn't exist.");
        }

        Bank bank = bankService.findByBankCode(card.getPAN().substring(0, 3));
        if(bank == null) {
            logger.error("Cannot register merchant - Bank requested code doesn't exist.");
            throw new Exception("Bank with this code doesn't exist.");
        }

        List<CardOwner> cardOwners = this.cardOwnerService.findAll();
        for (CardOwner co: cardOwners) {
            if(co.getCard().getId() == card.getId()) {
                // znaci da pokusava da tudju karticu predstavi kao svoju, ali mu ne smemo to reci kao info jer onda ima tudji pan
                logger.error("Cannot register merchant - invalid PAN.");
                throw new Exception("Cannot register merchant - invalid PAN.");
            }
        }

        Merchant newMerchant = new Merchant();
        newMerchant.setMerchantEmail(email);
        newMerchant.setCard(card);
        newMerchant.setBank(bank);
        newMerchant.setMerchantId(generateCommonLangPassword(6)); // 30 karaktera
        newMerchant.setPassword(generateCommonLangPassword(2)); // 10 karaktera

        Merchant saved = null;
        try {
            saved = save(newMerchant);
            logger.info("New merchant registered - merchant id: " + saved.getId());
        } catch (Exception e) {
            logger.error("Cannot register merchant - Saving merchant failed.");
            throw new Exception("Saving merchant failed.");
        }

        return saved;
    }

    private String generateCommonLangPassword(int count) {
        String upperCaseLetters = RandomStringUtils.random(count, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(count, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(count);
        String specialChar = RandomStringUtils.random(count, 33, 47, false, false);
        String totalChars = RandomStringUtils.randomAlphanumeric(count);
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
    public Merchant save(Merchant merchant) {
        return merchantRepository.save(merchant);
    }

}
