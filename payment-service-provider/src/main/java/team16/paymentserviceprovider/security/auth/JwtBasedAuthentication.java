package team16.paymentserviceprovider.security.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtBasedAuthentication extends AbstractAuthenticationToken {

    private String token;
    private final UserDetails principle;


    public JwtBasedAuthentication(UserDetails principle) {
        super(principle.getAuthorities());
        this.principle = principle;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principle;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }
}
