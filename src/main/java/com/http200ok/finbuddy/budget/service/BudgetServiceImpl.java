package com.http200ok.finbuddy.budget.service;

import com.http200ok.finbuddy.budget.domain.Budget;
import com.http200ok.finbuddy.budget.domain.PeriodType;
import com.http200ok.finbuddy.budget.dto.BudgetResponseDto;
import com.http200ok.finbuddy.budget.repository.BudgetRepository;
import com.http200ok.finbuddy.member.domain.Member;
import com.http200ok.finbuddy.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService{
    private final BudgetRepository budgetRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Long createMonthlyBudget(Long memberId, Long amount) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());

        Budget budget = Budget.createBudget(member, amount, PeriodType.MONTHLY, startDate, endDate);
        budgetRepository.save(budget);

        return budget.getId(); // Budget 대신 ID만 반환
    }

    @Override
    @Transactional
    public Long updateBudget(Long memberId, Long budgetId, Long newAmount) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예산이 존재하지 않습니다."));

        if (!budget.getMember().getId().equals(memberId)) {
            new AccessDeniedException("해당 예산을 수정할 권한이 없습니다.");
        }

        budget.setBudget(newAmount);
        return budget.getId(); // Budget 대신 ID만 반환
    }

    // 현재 월의 예산 조회 (내부 로직용 - Entity 반환)
    @Override
    public Optional<Budget> getCurrentMonthBudget(Long memberId) {
        LocalDate now = LocalDate.now();
        return budgetRepository.findByMemberIdAndStartDate(memberId, now.withDayOfMonth(1));
    }

    // 현재 월의 예산 조회 (화면 반환용 - DTO 변환)
    @Override
    public Optional<BudgetResponseDto> getCurrentMonthBudgetDto(Long memberId) {
        return getCurrentMonthBudget(memberId).map(BudgetResponseDto::fromEntity);
    }
}
