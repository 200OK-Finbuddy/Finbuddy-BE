package com.http200ok.finbuddy.batch.step;

import com.http200ok.finbuddy.account.repository.AccountRepository;
import com.http200ok.finbuddy.common.exception.InsufficientBalanceException;
import com.http200ok.finbuddy.transfer.domain.AutoTransfer;
import com.http200ok.finbuddy.transfer.repository.AutoTransferRepository;
import com.http200ok.finbuddy.transfer.service.AutoTransferService;
import com.http200ok.finbuddy.transfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AutoTransferTasklet implements Tasklet {

    private final AutoTransferRepository autoTransferRepository;
    private final TransferService transferService;
    private final AutoTransferService autoTransferService;
    private final AccountRepository accountRepository;
//    private final NotificationService notificationService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        // 주말(토, 일)이면 실행하지 않음
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            System.out.println("주말이므로 자동이체 실행 안 함.");
            return RepeatStatus.FINISHED;
        }

        // 월요일이면 지난 주말(토, 일) + 월요일 날짜의 자동이체 실행
        List<Integer> targetDays = (dayOfWeek == DayOfWeek.MONDAY)
                ? List.of(today.minusDays(2).getDayOfMonth(), today.minusDays(1).getDayOfMonth(), today.getDayOfMonth()) // 토, 일, 월
                : List.of(today.getDayOfMonth()); // 평일이면 오늘 날짜만 실행

        System.out.println("자동이체 실행 - 오늘 날짜: " + today + ", 실행 대상 날짜: " + targetDays);

        List<AutoTransfer> transfers = autoTransferRepository.findForScheduledExecution(targetDays);

        if (transfers.isEmpty()) {
            System.out.println("오늘 실행할 자동이체 없음.");
            return RepeatStatus.FINISHED;
        }

        for (AutoTransfer transfer : transfers) {
            try {
                transferService.executeAccountTransfer(
                        transfer.getAccount().getMember().getId(),
                        transfer.getAccount().getId(),
                        transfer.getTargetBankName(),
                        transfer.getTargetAccountNumber(),
                        transfer.getAmount(),
                        transfer.getAccount().getPassword(),
                        transfer.getAccount().getMember().getName(),
                        null
                );
                System.out.println("자동이체 성공 ID: " + transfer.getId());
            } catch (InsufficientBalanceException e) {
                System.out.println("자동이체 실패(잔액 부족) ID: " + transfer.getId());
                // 잔액 부족으로 자동이체 실패했다고 알림 보내기
                autoTransferService.markAsFailedAndSave(transfer);
            } catch (Exception e) {
                System.out.println("자동이체 실패(기타 오류) ID: " + transfer.getId());
                autoTransferService.markAsFailedAndSave(transfer);
            }
        }
        return RepeatStatus.FINISHED;
    }
}
