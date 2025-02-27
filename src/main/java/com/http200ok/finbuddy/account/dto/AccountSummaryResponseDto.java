package com.http200ok.finbuddy.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckingAccountResponseDto {
    private Long accountId;
    private String accountName;
    private String bankLogoUrl;
    private String accountNumber;
    private Long balance;
}
