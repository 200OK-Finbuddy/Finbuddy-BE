package com.http200ok.finbuddy.transaction.service;

import com.http200ok.finbuddy.budget.service.BudgetService;
import com.http200ok.finbuddy.category.dto.CategoryExpenseDto;
import com.http200ok.finbuddy.transaction.domain.Transaction;
import com.http200ok.finbuddy.transaction.dto.CheckingAccountTransactionResponseDto;
import com.http200ok.finbuddy.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BudgetService budgetService;

    @Override
    @Transactional(readOnly = true)
    public List<CheckingAccountTransactionResponseDto> getLatestTransactionsForUserCheckingAccounts(Long memberId) {
        Pageable pageable = PageRequest.of(0, 5);
        List<Transaction> transactions = transactionRepository
                .findLatestTransactionsForUserCheckingAccounts(memberId, pageable)
                .getContent(); // Page 객체에서 List로 변환

        return transactions.stream()
                .map(CheckingAccountTransactionResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryExpenseDto> categoryExpensesForMonth(Long memberId, int year, int month) {
        // 전체 소비 금액 조회
        Long totalAmount = transactionRepository.sumTotalAmountForMonth(memberId, year, month);
        if (totalAmount == null || totalAmount == 0) {
            return Collections.emptyList();
        }

        // 카테고리별 합계 조회
        List<CategoryExpenseDto> categorySums = transactionRepository.sumAmountByCategoryForMonth(memberId, year, month);

        // 각 카테고리별 금액을 전체 금액으로 나누어 비율 계산
        return categorySums.stream()
                .map(dto -> new CategoryExpenseDto(
                        dto.getCategoryName(),
                        dto.getTotalAmount(),
                        totalAmount > 0 ? (dto.getTotalAmount().doubleValue() / totalAmount.doubleValue()) * 100.0 : 0.0
                ))
                .collect(Collectors.toList());
    }

//    @Transactional
//    public Transaction createTransaction(Transaction transaction) {
//
//        // 1. 거래 저장
//        transactionRepository.save(transaction);
//
//        // 2. 출금(TransactionType = 2)일 경우 예산 초과 여부 확인 후 알림 전송
//        if (transaction.getTransactionType() == 2) {
//            checkAndNotifyBudgetExceededOnTransaction(transaction.getAccount().getMember().getId());
//        }
//
//        return transaction;
//    }

    // 이체 발생 시 즉시 예산 초과 여부 확인 및 알림 전송
    @Override
    public void checkAndNotifyBudgetExceededOnTransaction(Long memberId) {
        budgetService.getCurrentMonthBudget(memberId)
                .ifPresent(budget -> {
                    Long totalSpending = transactionRepository.getTotalSpendingForCurrentMonth(memberId);
                    if (totalSpending > budget.getBudget()) {
                        // notificationService.sendBudgetExceededNotification(budget.getMember());
                        System.out.println("예산초과");
                    } else {
                        System.out.println("예산이하");
                    }
                });
    }
}
