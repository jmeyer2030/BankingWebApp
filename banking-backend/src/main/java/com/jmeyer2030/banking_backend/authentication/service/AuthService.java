package com.jmeyer2030.banking_backend.authentication.service;

import com.jmeyer2030.banking_backend.authentication.jwt.JwtTokenProvider;
import com.jmeyer2030.banking_backend.exception.authentication.InvalidTokenException;
import com.jmeyer2030.banking_backend.user.dto.User;
import com.jmeyer2030.banking_backend.user.repository.UserRepository;
import com.jmeyer2030.banking_backend.user.service.PasswordService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    /**
    * Returns the userId given a token. Throws exception if token is invalid
    *
    * @param token authentication token
    * @returns userId if token is valid
    * @thows InvalidTokenException if token expired or isn't valid
    */
    public Long authenticateAndGetUserId(String token) {
        Objects.requireNonNull(token);

        // If token is bad, throw an exception
        if (!jwtTokenProvider.tokenIsValid(token)) {
            throw new InvalidTokenException();
        }

        // Else, token is valid
        String username = jwtTokenProvider.extractUsername(token);
        User user = Objects.requireNonNull(userRepository.findByUsername(username));

        return user.getId();
    }



}
