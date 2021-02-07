package team16.paymentserviceprovider.service;

import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.model.User;

public interface UserService {

    User findByEmail(String email);
    User update(Merchant merchant);
}
