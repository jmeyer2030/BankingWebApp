package com.jmeyer2030.banking_backend.authentication.controller;

import com.jmeyer2030.banking_backend.authentication.dto.LoginRequest;
import com.jmeyer2030.banking_backend.authentication.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
    * Receives form of a login attempt
    * Processes it a nd returns a response entity containing either:
    *  - Why it failed
    *  - The authentication token
    *
    * @param loginRequest the username and password of the login
    * @param result the result of trying to create the loginRequest object
    * @param
    * @return ResponseEntity the response to the frontend
    */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateLogin(@Valid @RequestBody LoginRequest loginRequest, BindingResult result, HttpServletResponse response) {


        // Check if form has errors as defined in LoginFormDTO
        if (result.hasErrors()) {
            // If so, collect errors and return as bad request
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        // Since no semantic errors, send to authService to generate a response
        return authService.authenticateUser(loginRequest, response);
    }
}
