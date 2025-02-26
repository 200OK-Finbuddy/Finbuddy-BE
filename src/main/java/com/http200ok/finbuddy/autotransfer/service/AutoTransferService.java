package com.http200ok.finbuddy.autotransfer.service;

import com.http200ok.finbuddy.autotransfer.domain.AutoTransfer;

public interface AutoTransferService {
    AutoTransfer createAutoTransfers(Long fromAccountId, String targetAccountNumber, Long amount, Integer transferDay);
}
