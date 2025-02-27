package com.http200ok.finbuddy.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AccountResponseDto {
    private List<CheckingAccountResponseDto> accounts;
}
