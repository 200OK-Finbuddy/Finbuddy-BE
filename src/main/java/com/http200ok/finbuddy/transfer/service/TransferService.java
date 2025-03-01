package com.http200ok.finbuddy.transfer.service;

import com.http200ok.finbuddy.account.dto.ReceivingAccountResponseDto;

public interface TransferService {
    void checkAndNotifyBudgetExceededOnTransaction(Long memberId);
    boolean executeAccountTransfer(Long memberId, Long fromAccountId, String toBankName, String toAccountNumber, Long amount, String password, String senderName, String receiverName);
    ReceivingAccountResponseDto getReceivingAccount(String bankName, String accountNumber);
}
