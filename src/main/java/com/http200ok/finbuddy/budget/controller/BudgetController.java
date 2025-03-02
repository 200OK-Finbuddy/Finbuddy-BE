package com.http200ok.finbuddy.budget.controller;

import com.http200ok.finbuddy.budget.dto.BudgetCreateRequestDto;
import com.http200ok.finbuddy.budget.dto.BudgetUpdateRequestDto;
import com.http200ok.finbuddy.budget.service.BudgetService;
import com.http200ok.finbuddy.transaction.service.TransactionService;
import com.http200ok.finbuddy.transfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<Long> createMonthlyBudget(@RequestBody BudgetCreateRequestDto requestDto) {
        Long budgetId = budgetService.createMonthlyBudget(requestDto.getMemberId(), requestDto.getAmount());
        return ResponseEntity.ok(budgetId);
    }

    @PatchMapping("/{budgetId}")
    public ResponseEntity<Long> updateBudget(@PathVariable("budgetId") Long budgetId, @RequestBody BudgetUpdateRequestDto requestDto) {
        Long updatedBudgetId = budgetService.updateBudget(requestDto.getMemberId(), budgetId, requestDto.getNewAmount());
        return ResponseEntity.ok(updatedBudgetId);
    }

    // 예산 초과 테스트 API (GET 요청)
    @GetMapping("/test-exceeded/{memberId}")
    public ResponseEntity<String> testExceededBudget(@PathVariable("memberId") Long memberId) {
        budgetService.checkAndNotifyBudgetExceededOnTransaction(memberId);
        return ResponseEntity.ok("예산 초과 체크를 수행했습니다.");
    }

}
