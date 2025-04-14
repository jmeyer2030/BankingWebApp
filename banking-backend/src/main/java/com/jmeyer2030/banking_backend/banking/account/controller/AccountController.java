package com.jmeyer2030.banking_backend.banking.account.controller;

import com.jmeyer2030.banking_backend.banking.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
    * Returns account information given a token
    * @param token
    *
    * @return ResponseEntity<AccountResponse> if valid token, else bad request
    *
    */
    @PostMapping("/account")
    public ResponseEntity<?> getAccountInfo(@CookieValue(name = "accountToken", required = false) String token) {
        return accountService.getAccountInformation(token);
    }

}
