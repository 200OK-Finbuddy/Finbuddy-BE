package com.http200ok.finbuddy.transaction.dto;

import com.http200ok.finbuddy.transaction.domain.Transaction;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CheckingAccountTransactionResponseDto {  // 이름 변경
    private final String opponentName;
    private final Long amount;
    private final Long updatedBalance;
    private final LocalDateTime transactionDate;

    public CheckingAccountTransactionResponseDto(Transaction transaction) {
        this.opponentName = transaction.getOpponentName();
        this.amount = transaction.getAmount();
        this.updatedBalance = transaction.getUpdatedBalance();
        this.transactionDate = transaction.getTransactionDate();
    }
}
