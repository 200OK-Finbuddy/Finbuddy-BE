package com.http200ok.finbuddy.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CheckingAccountSummaryResponseDto {
    private Long totalBalance;
    private List<CheckingAccountResponseDto> top3Accounts;
}
