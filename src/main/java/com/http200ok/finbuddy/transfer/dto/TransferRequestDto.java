package com.http200ok.finbuddy.transfer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequestDto {
    private String fromAccountNumber;
    private String toAccountNumber;
    private Long amount;
    private String password;
    private String senderName;
    private String receiverName;

}
