package team16.bitcoinservice.service.impl;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.bitcoinservice.dto.FormFieldDTO;
import team16.bitcoinservice.dto.FormFieldType;
import team16.bitcoinservice.model.Merchant;
import team16.bitcoinservice.repository.MerchantRepository;
import team16.bitcoinservice.service.MerchantService;

import java.util.ArrayList;
import java.util.List;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    Logger logger = LoggerFactory.getLogger(MerchantServiceImpl.class);

    @Override
    public Merchant findByEmail(String email) {
        return this.merchantRepository.findByEmail(email);
    }

    @Override
    public List<FormFieldDTO> getFormFields() {

        List<FormFieldDTO> formFields = new ArrayList<>();
        formFields.add(new FormFieldDTO("token", "Token", FormFieldType.text, true));
        return formFields;
    }

    @Override
    public Merchant addNewMerchant(String merchantData, String email) {

        Gson gson = new Gson();
        try {
            Merchant merchant = gson.fromJson(merchantData, Merchant.class);
            merchant.setEmail(email);
            return this.merchantRepository.save(merchant);
        }catch(Exception e){
            logger.error("Invalid merchant data");
            return null;
        }
    }
}
