package com.http200ok.finbuddy.transaction.service;

import com.http200ok.finbuddy.transaction.Repository.TransactionRepository;
import com.http200ok.finbuddy.transaction.domain.Transaction;
import com.http200ok.finbuddy.transaction.dto.CheckingAccountTransactionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CheckingAccountTransactionResponseDto> getLatestTransactionsForUserCheckingAccounts(Long memberId) {
        List<Transaction> transactions = transactionRepository.findLatestTransactionsForUserCheckingAccounts(memberId)
                .stream()
                .limit(5)
                .collect(Collectors.toList());

        return transactions.stream()
                .map(CheckingAccountTransactionResponseDto::new)
                .collect(Collectors.toList());
    }
}
