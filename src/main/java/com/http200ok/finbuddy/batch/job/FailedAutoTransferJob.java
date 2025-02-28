package com.http200ok.finbuddy.batch.job;

import com.http200ok.finbuddy.transfer.domain.AutoTransfer;
import com.http200ok.finbuddy.transfer.domain.AutoTransferStatus;
import com.http200ok.finbuddy.transfer.repository.AutoTransferRepository;
import com.http200ok.finbuddy.transfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FailedAutoTransferJob {

    private final JobLauncher jobLauncher;
    private final Job autoTransferJob;
    private final AutoTransferRepository autoTransferRepository;
    private final TransferService transferService;

    @Scheduled(cron = "0 0 * * * ?")
    public void retryFailedAutoTransfers() {
        List<AutoTransfer> failedTransfers = autoTransferRepository.findByStatus(AutoTransferStatus.FAILED);

        if (failedTransfers.isEmpty()) {
            System.out.println("재시도할 실패한 자동이체 없음.");
            return;
        }

        for (AutoTransfer transfer : failedTransfers) {
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

                if (success) {
                    transfer.markAsActive();
                    autoTransferRepository.save(transfer);
                    System.out.println("자동이체 재시도 성공 ID: " + transfer.getId());
                } else {
                    System.out.println("자동이체 재시도 실패 ID: " + transfer.getId());
                }
            } catch (Exception e) {
                System.out.println("자동이체 재시도 중 오류 발생 ID " + transfer.getId());
            }
        }
    }
}
