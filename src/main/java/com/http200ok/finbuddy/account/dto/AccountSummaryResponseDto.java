package com.http200ok.finbuddy.account.dto;

import com.http200ok.finbuddy.account.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountSummaryResponseDto {
    private Long accountId;
    private String accountName;
    private String bankLogoUrl;
    private String accountNumber;
    private Long balance;

    public static AccountSummaryResponseDto from(Account account) {
        return new AccountSummaryResponseDto(
                account.getId(),
                account.getAccountName(),
                account.getBank().getLogoUrl(),
                account.getAccountNumber(),
                account.getBalance()
        );
    }
}
