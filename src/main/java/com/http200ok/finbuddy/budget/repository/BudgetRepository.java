package com.http200ok.finbuddy.budget.repository;

import com.http200ok.finbuddy.budget.domain.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    // 현재 월의 예산이 설정되어 있는지 확인 (startDate가 이번 달 1일인지 확인)
    boolean existsByMemberIdAndStartDate(Long memberId, LocalDate startDate);

    // 현재 월의 예산 조회 (startDate가 이번 달 1일인지 확인)
    Optional<Budget> findByMemberIdAndStartDate(Long memberId, LocalDate startDate);
}
