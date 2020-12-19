package team16.paymentserviceprovider.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.paymentserviceprovider.dto.OrderDTO;
import team16.paymentserviceprovider.service.TestService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/test")
public class TestController {

    @Autowired
    private TestService testService;

    Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping
    ResponseEntity<?> testMethod(){

        System.out.println("Success");
        return ResponseEntity.ok("https://localhost:3001");

    }

    @PostMapping
    ResponseEntity<?> testMethod(@RequestBody OrderDTO dto){
        // proveri info o merchant-u sa onim sto imas u bazi
        System.out.println("Success");
        return ResponseEntity.ok("http://localhost:3001");

    }

    @GetMapping("/available-services")
    ResponseEntity<?> getAvailableServices(){

        List<String> servicesNames = testService.getAvailableServices();
        logger.info("Returning all available services");
        return new ResponseEntity<>(servicesNames, HttpStatus.OK);

    }
}
