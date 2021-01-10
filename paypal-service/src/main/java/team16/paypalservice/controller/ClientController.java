package team16.paypalservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import team16.paypalservice.model.Client;
import team16.paypalservice.service.ClientService;

@RestController
@RequestMapping(value="/api/merchant")
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = "/formFields")
    public ResponseEntity getFormFields(){

        return new ResponseEntity(this.clientService.getFormFields(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addNewClient(@RequestHeader("Authorization") String authToken, @RequestBody String clientData){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
        String email = "";

        try {
            ResponseEntity<String> response = restTemplate.exchange("https://localhost:8083/psp-service/api/merchant/current", HttpMethod.GET, httpEntity, String.class);
            email = response.getBody();
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Error occurred while authenticating merchant.");
        }

        Client client = this.clientService.findByEmail(email);
        if(client != null){
            return ResponseEntity.badRequest().body("This merchant has already chosen PayPal payment method.");
        }

        Client newClient = this.clientService.addNewClient(clientData, email);

        if(newClient == null){
            return ResponseEntity.badRequest().body("Invalid merchant data.");
        }

        return ResponseEntity.ok().build();

    }

}
