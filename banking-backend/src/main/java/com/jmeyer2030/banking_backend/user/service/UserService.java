package com.jmeyer2030.banking_backend.user.service;

import com.jmeyer2030.banking_backend.authentication.JwtTokenProvider;
import com.jmeyer2030.banking_backend.exception.IncorrectPasswordException;
import com.jmeyer2030.banking_backend.exception.UserDoesNotExistException;
import com.jmeyer2030.banking_backend.authentication.dto.LoginRequest;
import com.jmeyer2030.banking_backend.user.repository.UserRepository;
import com.jmeyer2030.banking_backend.exception.EmailAlreadyExistsException;
import com.jmeyer2030.banking_backend.user.dto.NewUserFormDTO;
import com.jmeyer2030.banking_backend.user.dto.UserDTO;
import com.jmeyer2030.banking_backend.exception.UsernameAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service class, that should handle all the business logic relating to user
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final JwtTokenProvider jwtTokenProvider;


    @Autowired
    public UserService(UserRepository userRepository, PasswordService passwordService, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Registers the user by:
     * - Validating uniqueness of username/email
     * - Creating a new UserDTO
     * - Submitting it to user repository
     *
     * @param formData the user submitted form
     * @throws UsernameAlreadyExistsException if the username is not unique
     * @throws EmailAlreadyExistsException    if the email is not unique
     */
    public void registerUser(NewUserFormDTO formData) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        // Validate that username is unique
        if (userRepository.existsByUsername(formData.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        // Validate that email is unique
        if (userRepository.existsByEmail(formData.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        // Create UserDTO, hashing the password
        UserDTO user = new UserDTO(formData.getUsername(),
                passwordService.encodePassword(formData.getPassword()),
                formData.getFirstname(),
                formData.getLastname(),
                formData.getEmail());

        // Add to database
        userRepository.save(user);
    }
}
