package team16.paypalservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/test")
public class TestController {

    @GetMapping
    ResponseEntity<?> testMethod(){
        System.out.println("Success");
        return ResponseEntity.ok("Hello from PayPal Service!");
    }
}
