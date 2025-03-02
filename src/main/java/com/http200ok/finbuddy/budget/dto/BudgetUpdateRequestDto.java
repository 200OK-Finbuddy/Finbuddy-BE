package com.http200ok.finbuddy.budget.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetUpdateRequestDto {
    private Long newAmount;
}
