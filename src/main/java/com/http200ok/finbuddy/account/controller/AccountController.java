package com.http200ok.finbuddy.account.controller;

import com.http200ok.finbuddy.account.dto.AccountDetailsResponse;
import com.http200ok.finbuddy.account.dto.AccountResponseDto;
import com.http200ok.finbuddy.account.dto.CheckingAccountSummaryResponseDto;
import com.http200ok.finbuddy.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{memberId}/{accountId}")
    public ResponseEntity<AccountDetailsResponse> getAccountDetails(@PathVariable("memberId") Long memberId,
                                                                    @PathVariable("accountId") Long accountId) {
        AccountDetailsResponse response = accountService.getAccountDetails(memberId, accountId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/checking/{memberId}")
    public ResponseEntity<CheckingAccountSummaryResponseDto> getCheckingAccounts(@PathVariable("memberId") Long memberId) {
        CheckingAccountSummaryResponseDto response = accountService.getCheckingAccountsSummary(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all/{memberId}")
    public ResponseEntity<AccountResponseDto> getAllAccounts(@PathVariable("memberId") Long memberId) {
        AccountResponseDto response = accountService.getAllAccounts(memberId);
        return ResponseEntity.ok(response);
    }
}
