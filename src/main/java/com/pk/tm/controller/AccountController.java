package com.pk.tm.controller;

import com.pk.tm.entity.Account;
import com.pk.tm.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/create-account")
    public ResponseEntity<String> createAccount(@RequestBody Account account){
        accountService.createAccount(account);
        return ResponseEntity.ok("account created successfully...");
    }
}
