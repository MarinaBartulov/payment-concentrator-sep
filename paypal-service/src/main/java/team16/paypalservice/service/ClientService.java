package team16.paypalservice.service;

import team16.paypalservice.model.Client;

public interface ClientService {

    Client findByEmail(String email);
}
