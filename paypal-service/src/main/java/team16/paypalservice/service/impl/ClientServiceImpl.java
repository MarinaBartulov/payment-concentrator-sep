package team16.paypalservice.service.impl;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.paypalservice.dto.FormFieldDTO;
import team16.paypalservice.dto.FormFieldType;
import team16.paypalservice.model.Client;
import team16.paypalservice.repository.ClientRepository;
import team16.paypalservice.service.ClientService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Override
    public Client findByEmail(String email) {

        return clientRepository.findByEmail(email);
    }

    @Override
    public List<FormFieldDTO> getFormFields() {
        List<FormFieldDTO> formFields = new ArrayList<>();
        formFields.add(new FormFieldDTO("clientId", "Client Id", FormFieldType.text, true));
        formFields.add(new FormFieldDTO("clientSecret", "Client Secret", FormFieldType.text, true));
        return formFields;
    }

    @Override
    public Client addNewClient(String clientData, String email) {
        Gson gson = new Gson();
        try {
            Client client = gson.fromJson(clientData, Client.class);
            client.setEmail(email);
            return this.clientRepository.save(client);
        }catch(Exception e){
            return null;
        }
    }
}
