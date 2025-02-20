package com.http200ok.finbuddy.account.service;

import com.http200ok.finbuddy.account.dto.AccountDetailsResponse;

public interface AccountService {
    AccountDetailsResponse getAccountDetails(Long memberId, Long accountId);
}
