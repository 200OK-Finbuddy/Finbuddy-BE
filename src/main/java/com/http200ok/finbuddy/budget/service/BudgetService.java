package com.http200ok.finbuddy.budget.service;

public interface BudgetService {
    Long createMonthlyBudget(Long memberId, Long amount);
    Long updateBudget(Long memberId, Long budgetId, Long newAmount);
}
