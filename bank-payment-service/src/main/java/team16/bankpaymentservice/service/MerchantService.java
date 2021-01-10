package team16.bankpaymentservice.service;

import team16.bankpaymentservice.dto.FormFieldDTO;
import team16.bankpaymentservice.model.Merchant;

import java.util.List;

public interface MerchantService {

    Merchant findByEmail(String email);
    List<FormFieldDTO> getFormFields();
    Merchant addNewMerchant(String merchantData, String email);
}
