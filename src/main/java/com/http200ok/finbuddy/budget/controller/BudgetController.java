package com.http200ok.finbuddy.budget.controller;

import com.http200ok.finbuddy.budget.dto.BudgetCreateRequestDto;
import com.http200ok.finbuddy.budget.dto.BudgetUpdateRequestDto;
import com.http200ok.finbuddy.budget.service.BudgetService;
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

}
