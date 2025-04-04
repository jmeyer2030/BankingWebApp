package com.jmeyer2030.banking_backend;

import com.jmeyer2030.banking_backend.authentication.jwt.JwtTokenProvider;
import com.jmeyer2030.banking_backend.banking.account.repository.AccountRepository;
import com.jmeyer2030.banking_backend.banking.transaction.dto.TransactionForm;
import com.jmeyer2030.banking_backend.banking.transaction.dto.TransactionType;
import com.jmeyer2030.banking_backend.banking.transaction.repository.TransactionRepository;
import com.jmeyer2030.banking_backend.banking.transaction.service.TransactionService;
import com.jmeyer2030.banking_backend.exception.authentication.InvalidTokenException;
import com.jmeyer2030.banking_backend.user.dto.User;
import com.jmeyer2030.banking_backend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;


    @Test
    void processTransaction_invalidToken_throwsException() {
        String token = "token";
        TransactionForm transactionForm = new TransactionForm();

        when(tokenProvider.tokenIsValid("token")).thenReturn(false);

        assertThrows(InvalidTokenException.class, () -> {
            transactionService.processTransaction(token, transactionForm);
        });
    }

    @Test
    void processTransaction_TransferRecipientUserDoesNotExist_throwsException() {
        String token = "token";
        TransactionForm transactionForm = new TransactionForm();
        transactionForm.setType(TransactionType.TRANSFER);
        transactionForm.setRecipientUsername("Alice");

        User user = new User(); // Recipient
        user.setId(1);

        when(tokenProvider.tokenIsValid("token")).thenReturn(true);

        when(tokenProvider.extractUsername("token")).thenReturn("Bob");
        when(userRepository.findByUsername("Bob")).thenReturn(user);



    }
}
