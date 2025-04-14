package com.jmeyer2030.banking_backend.unit;

import com.jmeyer2030.banking_backend.authentication.service.AuthService;
import com.jmeyer2030.banking_backend.banking.account.dto.Account;
import com.jmeyer2030.banking_backend.banking.account.dto.AccountResponse;
import com.jmeyer2030.banking_backend.banking.account.dto.AccountType;
import com.jmeyer2030.banking_backend.banking.account.repository.AccountRepository;
import com.jmeyer2030.banking_backend.banking.account.service.AccountService;
import com.jmeyer2030.banking_backend.banking.transaction.repository.TransactionRepository;
import com.jmeyer2030.banking_backend.banking.transaction.service.TransactionService;
import com.jmeyer2030.banking_backend.exception.account.AccountDoesNotExistException;
import com.jmeyer2030.banking_backend.exception.authentication.InvalidTokenException;
import com.jmeyer2030.banking_backend.user.dto.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AuthService authService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private AccountService accountService;

    @Test
    void getAccountInformation_successfulRequest_returnsAccountInformationOkResponse() {
        String token = "token";
        Long userId = Long.valueOf(12345);
        Account account = new Account(
                Long.valueOf(1),
                Long.valueOf(0),
                AccountType.CHECKING,
                OffsetDateTime.now(),
                new User());


        when(authService.verifyTokenAndGetUserId(token)).thenReturn(userId);
        when(accountRepository.findByUser_Id(userId)).thenReturn(Optional.of(account));

        ResponseEntity<AccountResponse> response = accountService.getAccountInformation(token);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        AccountResponse responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(account.getId(), responseBody.getAccountId());
        assertEquals(account.getAccountType(), responseBody.getAccountType());
        assertEquals(account.getBalance(), responseBody.getBalance());
        assertEquals(0, responseBody.getRecentTransactions().size());
    }

    @Test
    void getAccountInformation_invalidToken_throwsInvalidTokenException() {
        String token = "invalidToken";

        when(authService.verifyTokenAndGetUserId(token))
                .thenThrow(new InvalidTokenException("Invalid token"));

        assertThrows(InvalidTokenException.class, () -> {
            accountService.getAccountInformation(token);
        });
    }


    @Test
    void getAccountInformation_accountNotFound_throwsAccountDoesNotExistException() {
        String token = "token";
        Long userId = 12345L;

        when(authService.verifyTokenAndGetUserId(token)).thenReturn(userId);
        when(accountRepository.findByUser_Id(userId)).thenReturn(Optional.empty());

        assertThrows(AccountDoesNotExistException.class, () -> {
            accountService.getAccountInformation(token);
        });
    }
}
