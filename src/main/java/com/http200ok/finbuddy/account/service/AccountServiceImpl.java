package com.http200ok.finbuddy.account.service;

import com.http200ok.finbuddy.account.domain.Account;
import com.http200ok.finbuddy.account.dto.*;
import com.http200ok.finbuddy.account.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public CheckingAccountsSummaryResponseDto getCheckingAccountsSummary(Long memberId) {
        // 모든 CheckingAccount 가져오기
        List<Account> checkingAccounts = accountRepository.findCheckingAccountsByMemberId(memberId);

        // 잔고 합계 계산
        Long totalBalance = checkingAccounts.stream()
                .mapToLong(Account::getBalance)
                .sum();

        // 계좌 ID로 정렬된 리스트에서 상위 3개만 가져오기
        List<AccountSummaryResponseDto> top3Accounts = checkingAccounts.stream()
                .limit(3)
                .map(AccountSummaryResponseDto::from)
                .collect(Collectors.toList());

        return new CheckingAccountsSummaryResponseDto(totalBalance, top3Accounts);
    }

    @Override
    public List<AccountSummaryResponseDto> getAccountsByMemberId(Long memberId) {
        List<Account> accounts = accountRepository.findAccountsByMemberId(memberId);
        return accounts.stream()
                .map(AccountSummaryResponseDto::from)
                .collect(Collectors.toList());
    }

}
