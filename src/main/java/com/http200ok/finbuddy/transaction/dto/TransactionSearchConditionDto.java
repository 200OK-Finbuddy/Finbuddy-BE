package com.http200ok.finbuddy.transaction.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class TransactionSearchConditionDto {
    private Long memberId;
    private Long accountId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer transactionType;
}
