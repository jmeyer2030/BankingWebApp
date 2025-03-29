package com.jmeyer2030.banking_backend.user.controller;

import com.jmeyer2030.banking_backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
* Should handle http requests and responses
*
* Should NOT contain business logic.
*/


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


}
