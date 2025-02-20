package com.http200ok.finbuddy.account.service;

import com.http200ok.finbuddy.account.domain.Account;
import com.http200ok.finbuddy.account.dto.AccountDetailsResponse;
import com.http200ok.finbuddy.account.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDetailsResponse getAccountDetails(Long memberId, Long accountId) {
        Account account = accountRepository.findAccountWithTransactions(accountId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 계좌를 찾을 수 없습니다."));
        return new AccountDetailsResponse(account);

    }
}
