package com.http200ok.finbuddy.common.validator;

import com.http200ok.finbuddy.account.domain.Account;
import com.http200ok.finbuddy.account.repository.AccountRepository;
import com.http200ok.finbuddy.bank.domain.Bank;
import com.http200ok.finbuddy.bank.repository.BankRepository;
import com.http200ok.finbuddy.common.exception.InvalidTransactionException;
import com.http200ok.finbuddy.common.exception.UnauthorizedAccessException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountValidatorImpl implements AccountValidator{

    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;

    public AccountValidatorImpl(AccountRepository accountRepository, BankRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    public Account validateAndGetAccount(Long accountId, Long memberId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + accountId));

        if (!account.getMember().getId().equals(memberId)) {
            throw new UnauthorizedAccessException("Member " + memberId + " is not authorized to access account " + accountId);
        }

        return account;
    }

    @Override
    public void validateBankAccountMatch(String bankName, String accountNumber) {
        Bank bank = bankRepository.findByName(bankName)
                .orElseThrow(() -> new EntityNotFoundException("Bank not found with name: " + bankName));

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with number: " + accountNumber));

        if (!account.getBank().getId().equals(bank.getId())) {
            throw new InvalidTransactionException("Account " + accountNumber + " does not belong to bank " + bankName);
        }
    }

    // 파사드 패턴 적용, 이체 검증 로직 통합
    @Override
    public void validateAccountForTransfer(Long accountId, Long memberId, String bankName, String accountNumber) {
        validateAndGetAccount(accountId, memberId); // 계좌 소유권 검증
        validateBankAccountMatch(bankName, accountNumber); // 은행-계좌 연관성 검증
    }
}
