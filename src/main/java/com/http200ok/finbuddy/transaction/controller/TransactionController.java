package com.http200ok.finbuddy.transaction.controller;

import com.http200ok.finbuddy.category.dto.CategoryExpenseDto;
import com.http200ok.finbuddy.common.dto.PagedResponseDto;
import com.http200ok.finbuddy.transaction.dto.CheckingAccountTransactionResponseDto;
import com.http200ok.finbuddy.transaction.dto.MonthlyTransactionSummaryDto;
import com.http200ok.finbuddy.transaction.dto.TransactionResponseDto;
import com.http200ok.finbuddy.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
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

    // 카테고리별 지출 금액, 비율 조회(월, 계좌)
    @GetMapping("/account-category-expense")
    public ResponseEntity<List<CategoryExpenseDto>> getAccountCategoryExpense(
            @RequestParam("memberId") Long memberId,
            @RequestParam("accountId") Long accountId,
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ) {
        List<CategoryExpenseDto> expenses = transactionService.categoryExpensesForAccountAndMonth(memberId, accountId, year, month);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("account/{accountId}")
    public ResponseEntity<PagedResponseDto<TransactionResponseDto>> getTransactionsByAccountId(
            @PathVariable("accountId") Long accountId,
            @RequestParam("memberId") Long memberId,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(value = "transactionType", required = false) Integer transactionType,
            @PageableDefault(page = 0, size = 10, sort = "transactionDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<TransactionResponseDto> pagedTransactions = transactionService.getTransactionsByAccountId(accountId, memberId, startDate, endDate, transactionType, pageable);

        return ResponseEntity.ok(new PagedResponseDto<>(pagedTransactions));
    }

    @GetMapping("/account/monthly-summary")
    public ResponseEntity<MonthlyTransactionSummaryDto> getMonthlySummary(
            @RequestParam("memberId") Long memberId,
            @RequestParam("accountId") Long accountId,
            @RequestParam("year") int year,
            @RequestParam("month") int month) {

        MonthlyTransactionSummaryDto summary = transactionService.getMonthlyTransactionSummary(memberId, accountId, year, month);
        return ResponseEntity.ok(summary);
    }
}
