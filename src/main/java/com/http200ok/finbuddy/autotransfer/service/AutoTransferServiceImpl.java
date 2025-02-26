package com.http200ok.finbuddy.autotransfer.service;

import com.http200ok.finbuddy.account.domain.Account;
import com.http200ok.finbuddy.account.repository.AccountRepository;
import com.http200ok.finbuddy.autotransfer.domain.AutoTransfer;
import com.http200ok.finbuddy.autotransfer.repository.AutoTransferRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutoTransferServiceImpl implements AutoTransferService {
    private final AccountRepository accountRepository;
    private final AutoTransferRepository autoTransferRepository;

    @Override
    @Transactional
    public AutoTransfer createAutoTransfers(Long fromAccountId, String targetAccountNumber, Long amount, Integer transferDay) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new EntityNotFoundException("선택하신 출금 계좌를 찾을 수 없습니다."));

        // 입력한 입금 계좌 조회
        Account toAccount = accountRepository.findByAccountNumber(targetAccountNumber)
                .orElseThrow(() -> new EntityNotFoundException("입력하신 계좌번호의 계좌가 존재하지 않습니다."));

        // 자동이체 엔티티 생성
        AutoTransfer autoTransfer = AutoTransfer.createAutoTransfer(
                fromAccount,
                toAccount,
                amount,
                transferDay
        );

        return autoTransferRepository.save(autoTransfer);
    }

    // 특정 회원의 자동이체 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<AutoTransfer> getAutoTransfersByMember(Long memberId) {
        return autoTransferRepository.findByAccount_Member_Id(memberId);
    }
}
