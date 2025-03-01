package com.http200ok.finbuddy.account.controller;

import com.http200ok.finbuddy.account.dto.AccountResponseDto;
import com.http200ok.finbuddy.account.dto.AccountSummaryResponseDto;
import com.http200ok.finbuddy.account.dto.CheckingAccountResponseDto;
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

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDto> getAccountDetails(@PathVariable("accountId") Long accountId, @RequestParam("memberId") Long memberId) {
        AccountResponseDto accountDetails = accountService.getAccountDetails(memberId, accountId);
        return ResponseEntity.ok(accountDetails);
    }

    @GetMapping("/checking/{memberId}")
    public ResponseEntity<CheckingAccountsSummaryResponseDto> getCheckingAccountsTop3(@PathVariable("memberId") Long memberId) {
        CheckingAccountsSummaryResponseDto checkingAccountsSummary = accountService.getCheckingAccountsSummary(memberId);
        return ResponseEntity.ok(checkingAccountsSummary);
    }

    @GetMapping("/all/checking")
    public ResponseEntity<List<CheckingAccountResponseDto>> getCheckingAccounts(@RequestParam("memberId") Long memberId) {
        List<CheckingAccountResponseDto> checkingAccountList = accountService.getCheckingAccountList(memberId);
        return ResponseEntity.ok(checkingAccountList);
    }

    @GetMapping("/all/{memberId}")
    public ResponseEntity<List<AccountSummaryResponseDto>> getAllAccounts(@PathVariable("memberId") Long memberId) {
        List<AccountSummaryResponseDto> accounts = accountService.getAccountsByMemberId(memberId);
        return ResponseEntity.ok(accounts);
    }
}
