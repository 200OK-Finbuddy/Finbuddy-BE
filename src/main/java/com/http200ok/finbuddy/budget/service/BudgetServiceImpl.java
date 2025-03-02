package com.http200ok.finbuddy.budget.service;

import com.http200ok.finbuddy.budget.domain.Budget;
import com.http200ok.finbuddy.budget.domain.PeriodType;
import com.http200ok.finbuddy.budget.dto.BudgetResponseDto;
import com.http200ok.finbuddy.budget.repository.BudgetRepository;
import com.http200ok.finbuddy.common.validator.BudgetValidator;
import com.http200ok.finbuddy.member.domain.Member;
import com.http200ok.finbuddy.member.repository.MemberRepository;
import com.http200ok.finbuddy.notification.domain.NotificationType;
import com.http200ok.finbuddy.notification.service.NotificationService;
import com.http200ok.finbuddy.transaction.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetValidator budgetValidator;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public Long createMonthlyBudget(Long memberId, Long amount) {
        Member member = budgetValidator.validateMemberAndCheckDuplicateBudget(memberId);

        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());

        Budget budget = Budget.createBudget(member, amount, PeriodType.MONTHLY, startDate, endDate);
        budgetRepository.save(budget);

        return budget.getId();
    }

    @Override
    @Transactional
    public Long updateBudget(Long memberId, Long budgetId, Long newAmount) {
        Budget budget = budgetValidator.validateAndGetBudget(budgetId, memberId);
        budget.setAmount(newAmount);
        return budget.getId();
    }

    @Override
    @Transactional
    public void deleteBudget(Long memberId, Long budgetId) {
        Budget budget = budgetValidator.validateAndGetBudget(budgetId, memberId);
        budgetRepository.delete(budget);
    }

    // 현재 월의 예산 조회 (화면 반환용 - DTO 변환)
    @Override
    @Transactional(readOnly = true)
    public Optional<BudgetResponseDto> getCurrentMonthBudgetDto(Long memberId) {
        return getCurrentMonthBudget(memberId).map(BudgetResponseDto::fromEntity);
    }

    // 현재 월의 예산 조회 (내부 로직용 - Entity 반환)
    private Optional<Budget> getCurrentMonthBudget(Long memberId) {
        LocalDate now = LocalDate.now();
        return budgetRepository.findByMemberIdAndStartDate(memberId, now.withDayOfMonth(1));
    }

    // 이체 발생 즉시 예산 초과 여부 확인 및 알림 전송
    @Override
    public void checkAndNotifyBudgetExceededOnTransaction(Long memberId) {
        getCurrentMonthBudget(memberId)
                .ifPresent(budget -> {
                    Long totalSpending = transactionRepository.getTotalSpendingForCurrentMonth(memberId);
                    Long budgetLimit = budget.getAmount();

                    if (totalSpending > budgetLimit) {
                        Long exceededAmount = totalSpending - budgetLimit;
                        int currentMonth = LocalDate.now().getMonthValue();

                        String message = String.format(
                                "%d월 예산을 %,d원 초과하였습니다. (예산: %,d원, 현재 지출: %,d원)",
                                currentMonth, exceededAmount, budgetLimit, totalSpending
                        );

                        notificationService.sendNotification(
                                budget.getMember(), NotificationType.BUDGET, message
                        );
                    }
                });
    }
}
