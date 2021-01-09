package team16.paymentserviceprovider.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import team16.paymentserviceprovider.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class TokenUtils {

    @Value("Payment Concentrator")
    private String APP_NAME;

    @Value("somesecret")
    public String SECRET;

    @Value("3600000")
    private int EXPIRES_IN;

    @Value("Authorization")
    private String AUTH_HEADER;

    @Autowired
    TimeProvider timeProvider;

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public String generateToken(String email) {
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setSubject(email)
                .setIssuedAt(timeProvider.now())
                .setExpiration(generateExpirationDate())
                .signWith(SIGNATURE_ALGORITHM, SECRET).compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public String getEmailFromToken(String token) {
        String email;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            email = claims.getSubject();
        } catch (Exception e) {
            email = null;
        }
        return email;
    }

    public Date getIssuedAtDateFromToken(String token) {
        Date issueAt;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            issueAt = claims.getIssuedAt();
        } catch (Exception e) {
            issueAt = null;
        }
        return issueAt;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public String getToken(HttpServletRequest request) {
        String authHeader = getAuthHeaderFromHeader(request);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }


    private Date generateExpirationDate() {
        return new Date(timeProvider.now().getTime() + EXPIRES_IN);
    }

    public int getExpiredIn() {
        return EXPIRES_IN;
    }


    public String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        User user = (User) userDetails;
        final String email = getEmailFromToken(token);

        return (email != null && email.equals(((User) userDetails).getUsername()));
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = this.getExpirationDateFromToken(token);
        if (expiration == null)
            return true;
        return expiration.before(new Date());
    }




}
