package com.http200ok.finbuddy.account.controller;

import com.http200ok.finbuddy.account.dto.AccountDetailsResponse;
import com.http200ok.finbuddy.account.dto.AccountSummaryResponseDto;
import com.http200ok.finbuddy.account.dto.CheckingAccountsSummaryResponseDto;
import com.http200ok.finbuddy.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<CheckingAccountsSummaryResponseDto> getCheckingAccounts(@PathVariable("memberId") Long memberId) {
        CheckingAccountsSummaryResponseDto response = accountService.getCheckingAccountsSummary(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all/{memberId}")
    public ResponseEntity<List<AccountSummaryResponseDto>> getAllAccounts(@PathVariable("memberId") Long memberId) {
        List<AccountSummaryResponseDto> accounts = accountService.getAccountsByMemberId(memberId);
        return ResponseEntity.ok(accounts);
    }
}
