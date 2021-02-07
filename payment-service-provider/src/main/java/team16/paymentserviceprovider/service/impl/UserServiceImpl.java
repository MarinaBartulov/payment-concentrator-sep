package team16.paymentserviceprovider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.model.User;
import team16.paymentserviceprovider.repository.UserRepository;
import team16.paymentserviceprovider.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User update(Merchant merchant) {
        return userRepository.save(merchant);
    }
}
