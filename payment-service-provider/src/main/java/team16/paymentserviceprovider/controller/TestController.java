package team16.paymentserviceprovider.controller;

import com.netflix.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team16.paymentserviceprovider.service.TestService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/test")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping
    ResponseEntity<?> testMethod(){

        System.out.println("Success");
        return ResponseEntity.ok("http://localhost:3001");

    }

    @GetMapping("/available-services")
    ResponseEntity<?> getAvailableServices(){

        List<String> servicesNames = testService.getAvailableServices();
        return new ResponseEntity<>(servicesNames, HttpStatus.OK);

    }
}
