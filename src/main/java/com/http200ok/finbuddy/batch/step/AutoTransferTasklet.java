package com.http200ok.finbuddy.batch.step;

import com.http200ok.finbuddy.batch.exception.InsufficientBalanceException;
import com.http200ok.finbuddy.notification.service.NotificationService;
import com.http200ok.finbuddy.transfer.domain.AutoTransfer;
import com.http200ok.finbuddy.transfer.domain.AutoTransferStatus;
import com.http200ok.finbuddy.transfer.repository.AutoTransferRepository;
import com.http200ok.finbuddy.transfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AutoTransferTasklet implements Tasklet {

    private final AutoTransferRepository autoTransferRepository;
    private final TransferService transferService;
//    private final NotificationService notificationService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        int today = LocalDate.now().getDayOfMonth();
        System.out.println("자동이체 실행 - 오늘 날짜: " + today);

        List<AutoTransfer> transfers = autoTransferRepository.findByTransferDayAndStatus(today, AutoTransferStatus.ACTIVE);

        if (transfers.isEmpty()) {
            System.out.println("오늘 실행할 자동이체 없음.");
            return RepeatStatus.FINISHED;
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

                if (success) {
                    System.out.println("자동이체 성공 ID: " + transfer.getId());
                } else {
                    throw new InsufficientBalanceException("잔액 부족으로 자동이체 실패");
                }
            } catch (InsufficientBalanceException e) {
                System.out.println("자동이체 실패(잔액 부족) ID: " + transfer.getId());
                // 잔액 부족으로 자동이체 실패했다고 알림 보내기
                transfer.markAsFailed();
                autoTransferRepository.save(transfer);
            } catch (Exception e) {
                System.out.println("자동이체 실패(기타 오류) ID: " + transfer.getId());
                transfer.markAsFailed();
                autoTransferRepository.save(transfer);
            }
        }
        return RepeatStatus.FINISHED;
    }
}
