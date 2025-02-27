package com.http200ok.finbuddy.account.service;

import com.http200ok.finbuddy.account.dto.AccountDetailsResponse;
import com.http200ok.finbuddy.account.dto.AccountResponseDto;
import com.http200ok.finbuddy.account.dto.CheckingAccountSummaryResponseDto;

public interface AccountService {
    AccountDetailsResponse getAccountDetails(Long memberId, Long accountId);
    CheckingAccountSummaryResponseDto getCheckingAccountsSummary(Long memberId);
    AccountResponseDto getAllAccounts(Long memberId);
}
