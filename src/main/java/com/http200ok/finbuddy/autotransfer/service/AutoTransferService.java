package com.http200ok.finbuddy.autotransfer.service;

import com.http200ok.finbuddy.autotransfer.domain.AutoTransfer;

import java.util.List;

public interface AutoTransferService {
    AutoTransfer createAutoTransfers(Long fromAccountId, String targetAccountNumber, Long amount, Integer transferDay);
    List<AutoTransfer> getAutoTransfersByMember(Long memberId);
    void toggleAutoTransferStatus(Long autoTransferId);
}
