package team16.paymentserviceprovider.security;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TimeProvider {

    public Date now() {
        return new Date();
    }
}
