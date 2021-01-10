package team16.bankpaymentservice.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.bankpaymentservice.dto.FormFieldDTO;
import team16.bankpaymentservice.dto.FormFieldType;
import team16.bankpaymentservice.model.Merchant;
import team16.bankpaymentservice.repository.MerchantRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;


    @Override
    public Merchant findByEmail(String email) {
        return this.merchantRepository.findByMerchantEmail(email);
    }

    @Override
    public List<FormFieldDTO> getFormFields() {

        List<FormFieldDTO> formFields = new ArrayList<>();
        formFields.add(new FormFieldDTO("merchantId", "Merchant Id", FormFieldType.text, true));
        formFields.add(new FormFieldDTO("password", "Merchant Password", FormFieldType.password, true));
        return formFields;
    }

    @Override
    public Merchant addNewMerchant(String merchantData, String email) {
        Gson gson = new Gson();
        try {
            Merchant merchant = gson.fromJson(merchantData, Merchant.class);
            merchant.setMerchantEmail(email);
            return this.merchantRepository.save(merchant);
        }catch(Exception e){
            return null;
        }
    }
}
