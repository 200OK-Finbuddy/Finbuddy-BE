package com.http200ok.finbuddy.transfer.service;

import com.http200ok.finbuddy.account.dto.CheckingAccountResponseDto;
import com.http200ok.finbuddy.account.dto.ReceivingAccountResponseDto;

import java.util.List;

public interface TransferService {
    void checkAndNotifyBudgetExceededOnTransaction(Long memberId);
    boolean executeAccountTransfer(Long memberId, Long fromAccountId, String toBankName, String toAccountNumber, Long amount, String password, String senderName, String receiverName);
    List<CheckingAccountResponseDto> getCheckingAccountList(Long memberId);
    ReceivingAccountResponseDto getReceivingAccount(String bankName, String accountNumber);
}
