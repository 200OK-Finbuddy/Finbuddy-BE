package com.http200ok.finbuddy.account.controller;

import com.http200ok.finbuddy.account.dto.AccountDetailsResponse;
import com.http200ok.finbuddy.account.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{memberId}/{accountId}")
    public ResponseEntity<AccountDetailsResponse> getAccountDetails(
            @PathVariable("memberId") Long memberId,
            @PathVariable("accountId") Long accountId
    ) {
        AccountDetailsResponse response = accountService.getAccountDetails(memberId, accountId);
        return ResponseEntity.ok(response);
    }
}
