package team16.paymentserviceprovider.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team16.paymentserviceprovider.dto.AppDTO;
import team16.paymentserviceprovider.service.AppService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/app")
public class AppController {

    @Autowired
    private AppService appService;

    Logger logger = LoggerFactory.getLogger(AppController.class);


    @PostMapping
    public ResponseEntity addNewApp(@RequestBody @Valid AppDTO appDto){

          if(this.appService.findByEmail(appDto.getOfficialEmail()) != null){
              logger.warn("Application with email " + appDto.getOfficialEmail() + " already exists.");
              return ResponseEntity.badRequest().body("Application with that email already exists.");
          }
          AppDTO appDTO = this.appService.addNewApp(appDto);
          if(appDTO == null){
              return ResponseEntity.badRequest().body("Payment method doesn't exist.");
          }
          return ResponseEntity.ok(appDTO);
    }
}
