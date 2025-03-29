package com.jmeyer2030.banking_backend.user.controller;

import com.jmeyer2030.banking_backend.authentication.dto.LoginRequest;
import com.jmeyer2030.banking_backend.user.service.UserService;
import com.jmeyer2030.banking_backend.user.dto.NewUserFormDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/forms")
public class FormController {

    private final UserService userService;

    public FormController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Receives form of a new user
     * Processes it, returning a ResponseEntity of if they were added or not
     *
     * @param form   the form from the attempt to bind the form data to the NewUserFormDTO
     * @param result the result of this attempted binding
     * @return ResponseEntity the response that should be sent to the frontend
     */
    @PostMapping("/register")
    public ResponseEntity<?> addUser(@Valid @RequestBody NewUserFormDTO form, BindingResult result) {

        // Check if form has errors as defined in NewUserFormDTO
        if (result.hasErrors()) {
            // If so, collect errors and return as bad request
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errors);
        }

        // May throw an EmailAlreadyExistsException or a UsernameAlreadyExistsException
        // In this case, the global exception handler returns the appropriate response entity
        userService.registerUser(form);

        return ResponseEntity.ok("User registered successfully");
    }
}
