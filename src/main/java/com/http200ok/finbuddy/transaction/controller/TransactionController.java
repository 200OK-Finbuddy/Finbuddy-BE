package com.http200ok.finbuddy.transaction.controller;

import com.http200ok.finbuddy.category.dto.CategoryExpenseDto;
import com.http200ok.finbuddy.transaction.dto.CheckingAccountTransactionResponseDto;
import com.http200ok.finbuddy.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/checking/recent/{memberId}")
    public ResponseEntity<List<CheckingAccountTransactionResponseDto>> getLatestTransactionsForUserCheckingAccounts(@PathVariable("memberId") Long memberId) {
        List<CheckingAccountTransactionResponseDto> transactions = transactionService.getLatestTransactionsForUserCheckingAccounts(memberId);
        return ResponseEntity.ok(transactions);
    }

    // 카테고리별 지출 금액, 비율 조회
    @GetMapping("/category-expenses")
    public ResponseEntity<List<CategoryExpenseDto>> getCategoryExpenses(
            @RequestParam("memberId") Long memberId,
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ) {
        List<CategoryExpenseDto> expenses = transactionService.categoryExpensesForMonth(memberId, year, month);
        return ResponseEntity.ok(expenses);
    }
}
