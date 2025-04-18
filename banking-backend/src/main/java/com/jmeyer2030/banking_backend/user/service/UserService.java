package com.jmeyer2030.banking_backend.user.service;

import com.jmeyer2030.banking_backend.authentication.jwt.JwtTokenProvider;
import com.jmeyer2030.banking_backend.banking.account.dto.Account;
import com.jmeyer2030.banking_backend.banking.account.dto.AccountType;
import com.jmeyer2030.banking_backend.banking.account.repository.AccountRepository;
import com.jmeyer2030.banking_backend.user.dto.User;
import com.jmeyer2030.banking_backend.user.repository.UserRepository;
import com.jmeyer2030.banking_backend.exception.registration.EmailAlreadyExistsException;
import com.jmeyer2030.banking_backend.user.dto.NewUserFormDTO;
import com.jmeyer2030.banking_backend.exception.registration.UsernameAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

/**
 * A service class, that should handle all the business logic relating to user
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountRepository accountRepository;


    @Autowired
    public UserService(UserRepository userRepository, PasswordService passwordService, JwtTokenProvider jwtTokenProvider, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.accountRepository = accountRepository;
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

        User user = new User();
        user.setUsername(formData.getUsername());
        user.setPasswordHash(passwordService.encodePassword(formData.getPassword()));
        user.setFirstname(formData.getFirstname());
        user.setLastname(formData.getLastname());
        user.setEmail(formData.getEmail());
        userRepository.save(user);

        Account account = new Account();
        account.setAccountType(AccountType.CHECKING);
        account.setBalance(0L);
        account.setCreationDate(OffsetDateTime.now());
        account.setUser(user);

        // Add to database tables
        accountRepository.save(account);
    }
}
