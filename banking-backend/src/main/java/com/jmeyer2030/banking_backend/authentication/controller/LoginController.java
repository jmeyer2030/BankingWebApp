package com.jmeyer2030.banking_backend.authentication.controller;

import com.jmeyer2030.banking_backend.authentication.dto.LoginForm;
import com.jmeyer2030.banking_backend.authentication.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping()
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * Receives form of a login attempt
     * Processes it a nd returns a response entity containing either:
     *  - Why it failed
     *  - The authentication token
     *
     * @param loginForm the username and password of the login
     * @param result the result of trying to create the loginRequest object
     * @param
     * @return ResponseEntity the response to the frontend
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateLogin(@Valid @RequestBody LoginForm loginForm, BindingResult result, HttpServletResponse response) {

        // Check if form has errors as defined in LoginFormDTO
        if (result.hasErrors()) {
            // If so, collect errors and return as bad request
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        // Since no semantic errors, send to authService to generate a response
        return loginService.loginUser(loginForm, response);
    }

    /**
    * Sets the auth token cookie to empty, and the time to expire instantly
    * @param response that we add the cookie to
    * @return response that resets the auth cookie
    */
    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(HttpServletResponse response) {
        return loginService.logoutUser(response);
    }
}
