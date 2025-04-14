package com.jmeyer2030.banking_backend.unit;

import com.jmeyer2030.banking_backend.authentication.service.AuthService;
import com.jmeyer2030.banking_backend.banking.account.dto.Account;
import com.jmeyer2030.banking_backend.banking.account.repository.AccountRepository;
import com.jmeyer2030.banking_backend.banking.transaction.dto.Transaction;
import com.jmeyer2030.banking_backend.banking.transaction.dto.TransactionForm;
import com.jmeyer2030.banking_backend.banking.transaction.dto.TransactionType;
import com.jmeyer2030.banking_backend.banking.transaction.repository.TransactionRepository;
import com.jmeyer2030.banking_backend.banking.transaction.service.TransactionService;
import com.jmeyer2030.banking_backend.exception.authentication.InvalidTokenException;
import com.jmeyer2030.banking_backend.exception.transaction.InsufficientFundsException;
import com.jmeyer2030.banking_backend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void processTransaction_failedAuthentication_throwsAuthException() {
        String token = "Token";
        TransactionForm transactionForm = new TransactionForm();

        when(authService.verifyTokenAndGetUserId(token)).thenThrow(InvalidTokenException.class);

        assertThrows(InvalidTokenException.class, () -> transactionService.processTransaction(token, transactionForm));
    }

    @Test
    void shouldThrowInsufficientFundsException_whenBalanceTooLow() {
        Long userId = 1L;
        Account account = new Account();
        account.setBalance(Long.valueOf(0));

        TransactionForm form = new TransactionForm();
        form.setTransactionType(TransactionType.WITHDRAW);
        form.setAmount(100L);

        when(authService.verifyTokenAndGetUserId("token")).thenReturn(userId);
        when(accountRepository.findByUser_Id(userId)).thenReturn(Optional.of(account));

        assertThrows(InsufficientFundsException.class, () -> {
            transactionService.processTransaction("token", form);
        });
    }

    @Test
    void shouldProcessTransactionSuccessfully() {
        Long userId = 1L;
        Account account = new Account();
        account.setBalance(Long.valueOf(100));

        TransactionForm form = new TransactionForm();
        form.setTransactionType(TransactionType.WITHDRAW);
        form.setAmount(100L);
        form.setDescription("Test withdrawal");

        when(authService.verifyTokenAndGetUserId("token")).thenReturn(userId);
        when(accountRepository.findByUser_Id(userId)).thenReturn(Optional.of(account));

        ResponseEntity<?> response = transactionService.processTransaction("token", form);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(accountRepository).adjustBalance(account.getId(), -100L);
        verify(transactionRepository).save(any(Transaction.class));
    }
}
