package com.jmeyer2030.banking_backend.authentication.service;

import com.jmeyer2030.banking_backend.authentication.JwtTokenProvider;
import com.jmeyer2030.banking_backend.authentication.dto.LoginRequest;
import com.jmeyer2030.banking_backend.exception.IncorrectPasswordException;
import com.jmeyer2030.banking_backend.exception.UserDoesNotExistException;
import com.jmeyer2030.banking_backend.exception.UsernameAlreadyExistsException;
import com.jmeyer2030.banking_backend.user.dto.UserDTO;
import com.jmeyer2030.banking_backend.user.repository.UserRepository;
import com.jmeyer2030.banking_backend.user.service.PasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, PasswordService passwordService, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Authenticates a user from a login request
     *
     * @param loginRequest username and password of the login request
     * @return ResponseEntity with the token or incorrect username/password
     */
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) throws UsernameAlreadyExistsException, IncorrectPasswordException {
        // Check if an account with that username exists
        if (!userRepository.existsByUsername(loginRequest.getUsername())) {
            throw new UserDoesNotExistException(UserDoesNotExistException.Context.SIGN_IN);
        }

        // Check if the password is correct
        UserDTO user = userRepository.findByUsername(loginRequest.getUsername());
        if (!passwordService.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new IncorrectPasswordException();
        }

        // Get auth token (throws exception which is caught and returns a response)
        String token = jwtTokenProvider.generateToken(loginRequest.getUsername());

        return ResponseEntity.ok().body(token);
    }
}
