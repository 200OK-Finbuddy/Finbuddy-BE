package com.http200ok.finbuddy.transaction.service;

import com.http200ok.finbuddy.category.dto.CategoryExpenseDto;
import com.http200ok.finbuddy.transaction.dto.CheckingAccountTransactionResponseDto;

import java.util.List;

public interface TransactionService {
    List<CheckingAccountTransactionResponseDto> getLatestTransactionsForUserCheckingAccounts(Long memberId);
    List<CategoryExpenseDto> categoryExpensesForMonth(Long memberId, int year, int month);
}
