package com.http200ok.finbuddy.budget.repository;

import com.http200ok.finbuddy.budget.domain.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
}
