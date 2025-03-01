package com.http200ok.finbuddy.transfer.service;

import com.http200ok.finbuddy.account.domain.Account;
import com.http200ok.finbuddy.account.repository.AccountRepository;
import com.http200ok.finbuddy.transfer.domain.AutoTransfer;
import com.http200ok.finbuddy.transfer.domain.AutoTransferStatus;
import com.http200ok.finbuddy.transfer.dto.AutoTransferUpdateRequestDto;
import com.http200ok.finbuddy.transfer.repository.AutoTransferRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AutoTransferServiceImpl implements AutoTransferService {
    private final AccountRepository accountRepository;
    private final AutoTransferRepository autoTransferRepository;
    private final TransferService transferService;

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

    // 자동이체 정보 조회
    @Transactional(readOnly = true)
    public AutoTransfer getAutoTransferById(Long autoTransferId) {
        return autoTransferRepository.findById(autoTransferId)
                .orElseThrow(() -> new EntityNotFoundException("자동이체 정보를 찾을 수 없습니다."));
    }

    // 자동이체 정보 수정(금액 날짜)
    @Override
    @Transactional
    public void updateAutoTransfer(Long autoTransferId, AutoTransferUpdateRequestDto requestDto) {
        AutoTransfer autoTransfer = autoTransferRepository.findById(autoTransferId)
                .orElseThrow(() -> new EntityNotFoundException("자동이체 정보를 찾을 수 없습니다."));

        autoTransfer.updateTransferInfo(requestDto.getAmount(), requestDto.getTransferDay());
    }
    // 자동이체 상태 변경
    @Override
    @Transactional
    public void toggleAutoTransferStatus(Long autoTransferId) {
        AutoTransfer autoTransfer = autoTransferRepository.findById(autoTransferId)
                .orElseThrow(() -> new EntityNotFoundException("자동이체 정보를 찾을 수 없습니다."));

        autoTransfer.toggleActiveStatus();
    }

    @Override
    public void deleteAutoTransfer(Long autoTransferId) {
        AutoTransfer autoTransfer = autoTransferRepository.findById(autoTransferId)
                .orElseThrow(() -> new EntityNotFoundException("자동이체 정보가 존재하지 않습니다."));

        autoTransferRepository.delete(autoTransfer);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsFailedAndSave(AutoTransfer transfer) {
        transfer.markAsFailed();
        autoTransferRepository.save(transfer);
    }

    // 스케줄러
    @Transactional
    public void executeScheduledAutoTransfers() {
        int today = LocalDate.now().getDayOfMonth();
        System.out.println("자동이체 실행 - 오늘 날짜: " + today);

        List<AutoTransfer> transfers = autoTransferRepository.findByTransferDayAndStatus(today, AutoTransferStatus.ACTIVE);

        if (transfers.isEmpty()) {
            System.out.println("오늘 실행할 자동이체가 없습니다.");
            return;
        }

        for (AutoTransfer transfer : transfers) {
            try {
                boolean success = transferService.transferMoney(
                        transfer.getAccount().getMember().getId(),
                        transfer.getAccount().getAccountNumber(),
                        transfer.getTargetAccount().getAccountNumber(),
                        transfer.getAmount(),
                        transfer.getAccount().getPassword(),
                        transfer.getAccount().getMember().getName(),
                        transfer.getTargetAccount().getMember().getName()
                );

                if (!success) {
                    System.out.println("자동이체 성공 (ID: " + transfer.getId() + ")");
                } else {
                    System.out.println("자동이체 실패 (ID: " + transfer.getId() + ")");
                }

            } catch (Exception e) {
                System.out.println("자동이체 중 오류 발생 (ID: " + transfer.getId() + "): " + e.getMessage());
            }
        }
    }
}
