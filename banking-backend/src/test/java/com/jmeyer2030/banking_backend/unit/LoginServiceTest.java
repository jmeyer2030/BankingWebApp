package com.jmeyer2030.banking_backend.unit;

import com.jmeyer2030.banking_backend.authentication.dto.LoginForm;
import com.jmeyer2030.banking_backend.authentication.jwt.JwtTokenProvider;
import com.jmeyer2030.banking_backend.authentication.service.AuthService;
import com.jmeyer2030.banking_backend.authentication.service.LoginService;
import com.jmeyer2030.banking_backend.exception.login.IncorrectLoginCredentialsException;
import com.jmeyer2030.banking_backend.user.dto.User;
import com.jmeyer2030.banking_backend.user.repository.UserRepository;
import com.jmeyer2030.banking_backend.user.service.PasswordService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


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
    private LoginService loginService;


    @Test
    void authenticateUser_successfulLogin_setsCookieAndReturnsResponse() throws Exception {
        LoginForm loginForm = new LoginForm("alice", "correctPassword");
        String token = "mockToken";

        User fakeUser = new User();
        fakeUser.setUsername("alice");
        fakeUser.setPasswordHash("hashedPassword");

        when(userRepository.existsByUsername("alice")).thenReturn(true);
        when(userRepository.findByUsername("alice")).thenReturn(fakeUser);
        when(passwordService.matches("correctPassword", "hashedPassword")).thenReturn(true);
        when(jwtTokenProvider.generateToken("alice")).thenReturn(token);

        ResponseEntity<String> response = loginService.loginUser(loginForm, httpServletResponse);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login was successful!", response.getBody());

        String expectedCookie = "accountToken=" + token +
                "; HttpOnly" +
                "; SameSite=Lax" +
                //"; Secure" + // Not enabled for local dev
                "; Path=/" +
                "; Max-Age=3600";

        verify(httpServletResponse).setHeader("Set-Cookie", expectedCookie);
        verify(jwtTokenProvider).generateToken("alice");
    }

    @Test
    void authenticateUser_userDoesNotExist_throwsException() {
        LoginForm loginForm = new LoginForm("alice", "irrelevant");

        when(userRepository.existsByUsername("alice")).thenReturn(false);

        assertThrows(IncorrectLoginCredentialsException.class, () -> {
            loginService.loginUser(loginForm, httpServletResponse);
        });

        // So we know we exited from bad username
        verify(userRepository, never()).findByUsername(any());
    }


    @Test
    void authenticateUser_passwordIncorrect_throwsException() {
        LoginForm loginForm = new LoginForm("alice", "wrongPassword");

        User fakeUser = new User();
        fakeUser.setUsername("alice");
        fakeUser.setPasswordHash("hashedPassword");

        when(userRepository.existsByUsername("alice")).thenReturn(true);
        when(userRepository.findByUsername("alice")).thenReturn(fakeUser);
        when(passwordService.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        assertThrows(IncorrectLoginCredentialsException.class, () -> {
            loginService.loginUser(loginForm, httpServletResponse);
        });

        verify(passwordService).matches("wrongPassword", "hashedPassword");
        verify(jwtTokenProvider, never()).generateToken(any());
        verify(httpServletResponse, never()).setHeader(eq("Set-Cookie"), any());
    }
}
