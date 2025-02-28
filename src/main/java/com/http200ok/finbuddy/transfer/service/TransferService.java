package com.http200ok.finbuddy.transfer.service;

public interface TransferService {
    void checkAndNotifyBudgetExceededOnTransaction(Long memberId);
    boolean transferMoney(Long memberId, String fromAccountNumber, String toAccountNumber, Long amount, String password, String senderName, String receiverName);
}
