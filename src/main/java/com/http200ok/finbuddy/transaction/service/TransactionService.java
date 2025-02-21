package com.http200ok.finbuddy.transaction.service;

import com.http200ok.finbuddy.transaction.dto.CheckingAccountTransactionResponseDto;

import java.util.List;

public interface TransactionService {
    List<CheckingAccountTransactionResponseDto> getLatestTransactionsForUserCheckingAccounts(Long memberId);
}
