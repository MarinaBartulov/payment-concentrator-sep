package team16.paypalservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.paypalservice.model.Client;
import team16.paypalservice.repository.ClientRepository;
import team16.paypalservice.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Override
    public Client findByEmail(String email) {

        return clientRepository.findByEmail(email);
    }
}
