package team16.paymentserviceprovider.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import team16.paymentserviceprovider.dto.ChangePasswordDTO;
import team16.paymentserviceprovider.dto.JwtAuthenticationRequestDTO;
import team16.paymentserviceprovider.dto.UserTokenStateDTO;
import team16.paymentserviceprovider.model.User;
import team16.paymentserviceprovider.security.TokenUtils;
import team16.paymentserviceprovider.service.impl.CustomUserDetailsService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    Logger logger = LoggerFactory.getLogger(AuthController.class);


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody @Valid JwtAuthenticationRequestDTO authenticationRequest,
                                                       HttpServletResponse response) throws AuthenticationException {

        Authentication authentication = null;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()));
        }catch(Exception e){
            logger.error("Login failed. Invalid email or password.");
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid email or password.");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();
        String role = user.getRoles().iterator().next().getName();

        logger.info("Login successful. User's email " + user.getEmail() + ".");
        return ResponseEntity.ok(new UserTokenStateDTO(jwt, expiresIn, role));
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordDTO changePasswordDTO){

        this.userDetailsService.changePassword(changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
        return ResponseEntity.ok().build();
    }

}
