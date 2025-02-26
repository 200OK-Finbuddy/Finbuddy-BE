package com.http200ok.finbuddy.budget.dto;

import com.http200ok.finbuddy.budget.domain.Budget;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BudgetResponseDto {

    private Long budgetId;
    private Long budgetAmount;

    public static BudgetResponseDto fromEntity(Budget budget) {
        return new BudgetResponseDto(budget.getId(), budget.getBudget());
    }
}
