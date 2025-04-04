package com.jmeyer2030.banking_backend;

import com.jmeyer2030.banking_backend.authentication.jwt.JwtTokenProvider;
import com.jmeyer2030.banking_backend.authentication.service.AuthService;
import com.jmeyer2030.banking_backend.user.dto.User;
import com.jmeyer2030.banking_backend.user.repository.UserRepository;
import com.jmeyer2030.banking_backend.user.service.PasswordService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {
    // Fake version (doesn't execute real code)
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordService passwordService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletResponse httpServletResponse;

    // Real version!
    @InjectMocks
    private AuthService authService;


    @Test
    void authenticateUserAndGetUserId_validToken_returnsUserId() throws Exception {
        User user = new User();
        user.setUsername("alice");
        user.setId(Long.valueOf(12345));

        String token = "token";
        /*
        when(userRepository.existsByUsername("alice")).thenReturn(true);
        when(userRepository.findByUsername("alice")).thenReturn(user);
        when(passwordService.matches("password123", "hashed")).thenReturn(true);
        */
        when(jwtTokenProvider.tokenIsValid(token)).thenReturn(true);
        when(jwtTokenProvider.extractUsername(token)).thenReturn("alice");
        when(userRepository.findByUsername("alice")).thenReturn(user);

        Long userId = null;
        try {
            userId = authService.authenticateAndGetUserId(token);
        } catch (Exception e) {
            fail();
        }

        assertEquals(userId, Long.valueOf(12345));
    }
    /*
    @Test
    void authenticateUser_userDoesNotExist_throwsException() {
        LoginRequest loginRequest = new LoginRequest("alice", "irrelevant");

        when(userRepository.existsByUsername("alice")).thenReturn(false);

        assertThrows(IncorrectLoginCredentialsException.class, () -> {
            authService.authenticateUser(loginRequest, httpServletResponse);
        });

        verify(userRepository, never()).findByUsername(any());
        verify(passwordService, never()).matches(any(), any());
    }

    @Test
    void authenticateUser_passwordMismatch_throwsException() {
        LoginRequest loginRequest = new LoginRequest("alice", "wrongPassword");
        User user = new User();
        user.setUsername("alice");
        user.setPasswordHash("hashed");

        when(userRepository.existsByUsername("alice")).thenReturn(true);
        when(userRepository.findByUsername("alice")).thenReturn(user);
        when(passwordService.matches("wrongPassword", "hashed")).thenReturn(false);

        assertThrows(IncorrectLoginCredentialsException.class, () -> {
            authService.authenticateUser(loginRequest, httpServletResponse);
        });

        verify(jwtTokenProvider, never()).generateToken(any());
    }
    */

}
