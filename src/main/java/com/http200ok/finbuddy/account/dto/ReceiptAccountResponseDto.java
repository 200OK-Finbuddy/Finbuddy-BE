package com.http200ok.finbuddy.account.dto;

import com.http200ok.finbuddy.account.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReceiptAccountResponseDto {
    private Long accountId;
    private String bankName;
    private String receiverName;
    private String accountNumber;

    public static ReceiptAccountResponseDto from(Account account) {
        return new ReceiptAccountResponseDto(
                account.getId(),
                account.getBank().getName(),
                account.getMember().getName(),
                account.getAccountNumber()
        );
    }
}
