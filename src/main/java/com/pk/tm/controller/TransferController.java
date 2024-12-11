package com.pk.tm.controller;

import com.pk.tm.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TransferController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/transfer-declarative")
    public ResponseEntity<String> transferDeclarative(@RequestParam long fromAccountId,
                                                      @RequestParam long toAccountId,
                                                      @RequestParam double amount) {
        accountService.transferMoneyDeclarative(fromAccountId, toAccountId, amount);
        return ResponseEntity.ok("Transfer money successfully");
    }

    @PostMapping("/transfer-programmatic")
    public String transferMoneyProgrammatic(@RequestParam long fromAccountId,
                                            @RequestParam long toAccountId,
                                            @RequestParam double amount) {
        accountService.transferMoneyProgrammatic(fromAccountId, toAccountId, amount);
        return "Transfer successful";
    }
}
