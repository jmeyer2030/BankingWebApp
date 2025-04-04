package com.jmeyer2030.banking_backend.authentication.service;

import com.jmeyer2030.banking_backend.authentication.dto.LoginRequest;
import com.jmeyer2030.banking_backend.authentication.jwt.JwtTokenProvider;
import com.jmeyer2030.banking_backend.exception.login.IncorrectLoginCredentialsException;
import com.jmeyer2030.banking_backend.user.dto.User;
import com.jmeyer2030.banking_backend.user.repository.UserRepository;
import com.jmeyer2030.banking_backend.user.service.PasswordService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LoginService {


    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(UserRepository userRepository, PasswordService passwordService, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    /**
     * Authenticates a user from a login request
     *
     * @param loginRequest username and password of the login request
     * @param response we take the response that we are going to send so that we can set it's cookie
     * @return ResponseEntity with the token or incorrect username/password
     */
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest, HttpServletResponse response) throws IncorrectLoginCredentialsException {
        // Exit if user doesn't exist
        if (!userRepository.existsByUsername(loginRequest.getUsername())) {
            throw new IncorrectLoginCredentialsException();
        }

        // Exit if password is wrong
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (!passwordService.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new IncorrectLoginCredentialsException();
        }

        // Get auth token (throws exception which is caught and returns a response)
        String token = jwtTokenProvider.generateToken(loginRequest.getUsername());

        // Manually set cookie
        String cookie = "accountToken=" + token +
                "; HttpOnly" +
                "; SameSite=Lax" +
                //"; Secure" + // Not Secure because not https on local
                "; Path=/" +
                "; Max-Age=3600";

        response.setHeader("Set-Cookie", cookie);

        return ResponseEntity.ok().body(token);
    }
}
