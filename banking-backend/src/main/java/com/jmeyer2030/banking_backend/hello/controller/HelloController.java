package com.jmeyer2030.banking_backend.hello.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "Spring boot request to / successful!";
    }

    @GetMapping("/home")
    public String home() {
        return "Spring Boot request to /home successful!";
    }
}
