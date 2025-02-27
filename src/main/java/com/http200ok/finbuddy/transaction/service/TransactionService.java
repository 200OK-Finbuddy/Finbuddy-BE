package com.http200ok.finbuddy.transaction.service;

import com.http200ok.finbuddy.category.dto.CategoryExpenseDto;
import com.http200ok.finbuddy.transaction.dto.CheckingAccountTransactionResponseDto;
import com.http200ok.finbuddy.transaction.dto.TransactionResponseDto;
import com.http200ok.finbuddy.transaction.dto.TransactionSearchConditionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {
    List<CheckingAccountTransactionResponseDto> getLatestTransactionsForUserCheckingAccounts(Long memberId);
    List<CategoryExpenseDto> categoryExpensesForMonth(Long memberId, int year, int month);
    void checkAndNotifyBudgetExceededOnTransaction(Long memberId);

    Page<TransactionResponseDto> getTransactionsByAccountId(TransactionSearchConditionDto condition, Pageable pageable);
}

