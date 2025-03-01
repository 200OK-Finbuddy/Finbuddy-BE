package com.http200ok.finbuddy.account.service;

import com.http200ok.finbuddy.account.domain.Account;
import com.http200ok.finbuddy.account.dto.AccountResponseDto;
import com.http200ok.finbuddy.account.dto.AccountSummaryResponseDto;
import com.http200ok.finbuddy.account.dto.CheckingAccountsSummaryResponseDto;
import com.http200ok.finbuddy.account.repository.AccountRepository;
import com.http200ok.finbuddy.common.validator.AccountValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountValidator accountValidator;

    public AccountServiceImpl(AccountRepository accountRepository, AccountValidator accountValidator) {
        this.accountRepository = accountRepository;
        this.accountValidator = accountValidator;
    }

    @Override
    public AccountResponseDto getAccountDetails(Long memberId, Long accountId) {
        // AccountValidator를 사용하여 계좌 권한 검증 및 조회
        Account account = accountValidator.validateAndGetAccount(accountId, memberId);

        return AccountResponseDto.from(account);
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
