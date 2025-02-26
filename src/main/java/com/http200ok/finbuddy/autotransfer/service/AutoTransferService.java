package com.http200ok.finbuddy.autotransfer.service;

import com.http200ok.finbuddy.autotransfer.domain.AutoTransfer;
import com.http200ok.finbuddy.autotransfer.dto.AutoTransferUpdateRequestDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AutoTransferService {
    AutoTransfer createAutoTransfers(Long fromAccountId, String targetAccountNumber, Long amount, Integer transferDay);
    List<AutoTransfer> getAutoTransfersByMember(Long memberId);

    void updateAutoTransfer(Long autoTransferId, AutoTransferUpdateRequestDto requestDto);
    void toggleAutoTransferStatus(Long autoTransferId);
    void deleteAutoTransfer(Long autoTransferId);
}
