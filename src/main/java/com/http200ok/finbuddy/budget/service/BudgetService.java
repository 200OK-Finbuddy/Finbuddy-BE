package com.http200ok.finbuddy.budget.service;

import com.http200ok.finbuddy.budget.domain.Budget;
import com.http200ok.finbuddy.budget.dto.BudgetResponseDto;

import java.util.Optional;

public interface BudgetService {
    Long createMonthlyBudget(Long memberId, Long amount);
    Long updateBudget(Long memberId, Long budgetId, Long newAmount);

    Optional<BudgetResponseDto> getCurrentMonthBudgetDto(Long memberId);
    void checkAndNotifyBudgetExceededOnTransaction(Long memberId);
}
