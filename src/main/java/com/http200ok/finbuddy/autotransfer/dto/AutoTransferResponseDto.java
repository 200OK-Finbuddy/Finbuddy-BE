package com.http200ok.finbuddy.autotransfer.dto;

import com.http200ok.finbuddy.autotransfer.domain.AutoTransfer;
import lombok.Getter;

@Getter
public class AutoTransferResponseDto {

    private final Long id;
    private final String fromAccountNumber;
    private final String toAccountNumber;
    private final Long amount;
    private final Integer transferDay;
    private final String status;

    public AutoTransferResponseDto(AutoTransfer autoTransfer) {
        this.id = autoTransfer.getId();
        this.fromAccountNumber = autoTransfer.getAccount().getAccountNumber();
        this.toAccountNumber = autoTransfer.getTargetAccount().getAccountNumber();
        this.amount = autoTransfer.getAmount();
        this.transferDay = autoTransfer.getTransferDay();
        this.status = autoTransfer.getStatus().name();
    }
}
