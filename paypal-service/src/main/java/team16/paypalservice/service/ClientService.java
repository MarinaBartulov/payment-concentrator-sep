package team16.paypalservice.service;

import team16.paypalservice.dto.FormFieldDTO;
import team16.paypalservice.model.Client;

import java.util.List;

public interface ClientService {

    Client findByEmail(String email);
    List<FormFieldDTO> getFormFields();
    Client addNewClient(String clientData, String email);
}
