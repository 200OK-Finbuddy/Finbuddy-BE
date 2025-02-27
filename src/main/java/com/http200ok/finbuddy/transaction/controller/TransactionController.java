package com.http200ok.finbuddy.transaction.controller;

import com.http200ok.finbuddy.category.dto.CategoryExpenseDto;
import com.http200ok.finbuddy.transaction.dto.CheckingAccountTransactionResponseDto;
import com.http200ok.finbuddy.transaction.dto.TransactionResponseDto;
import com.http200ok.finbuddy.transaction.dto.TransactionSearchConditionDto;
import com.http200ok.finbuddy.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDto>> getTransactionsByAccountId(
            @ModelAttribute TransactionSearchConditionDto condition,
            @PageableDefault(page = 0, size = 10, sort = "transactionDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountId(condition, pageable));
    }
}
