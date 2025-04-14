package com.jmeyer2030.banking_backend.authentication.service;

import com.jmeyer2030.banking_backend.authentication.jwt.JwtTokenProvider;
import com.jmeyer2030.banking_backend.exception.authentication.InvalidTokenException;
import com.jmeyer2030.banking_backend.user.dto.User;
import com.jmeyer2030.banking_backend.user.repository.UserRepository;
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
    * @param token the authentication token
    * @return the userId if the token is valid
    * @throws InvalidTokenException if token is expired or invalid
    */
    public Long verifyTokenAndGetUserId(String token) {
        if (token == null) {
            throw new InvalidTokenException("Login to access the home page.");
        }

        // If token is bad, throw an exception
        if (!jwtTokenProvider.tokenIsValid(token)) {
            throw new InvalidTokenException();
        }

        // Else, token is valid
        String username = jwtTokenProvider.extractUsername(token);

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new InvalidTokenException();
        }

        return user.getId();
    }
}
