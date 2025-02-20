package com.http200ok.finbuddy.transaction.dto;

import com.http200ok.finbuddy.transaction.domain.Transaction;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TransactionResponse {

    private Long transactionId;
    private Integer transactionType; // 1: 입금, 2: 출금
    private Long amount;
    private Long updatedBalance;
    private LocalDateTime transactionDate;
    private String categoryName;

    public TransactionResponse(Transaction transaction) {
        this.transactionId = transaction.getId();
        this.transactionType = transaction.getTransactionType();
        this.amount = transaction.getAmount();
        this.updatedBalance = transaction.getUpdatedBalance();
        this.transactionDate = transaction.getTransactionDate();
        this.categoryName = transaction.getCategory().getCategoryName();
    }
}
