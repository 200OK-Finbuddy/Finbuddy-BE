package com.http200ok.finbuddy.transaction.service;

import com.http200ok.finbuddy.category.dto.CategoryExpenseDto;
import com.http200ok.finbuddy.transaction.repository.TransactionRepository;
import com.http200ok.finbuddy.transaction.domain.Transaction;
import com.http200ok.finbuddy.transaction.dto.CheckingAccountTransactionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CheckingAccountTransactionResponseDto> getLatestTransactionsForUserCheckingAccounts(Long memberId) {
        List<Transaction> transactions = transactionRepository.findLatestTransactionsForUserCheckingAccounts(memberId)
                .stream()
                .limit(5)
                .collect(Collectors.toList());

        return transactions.stream()
                .map(CheckingAccountTransactionResponseDto::new)
                .collect(Collectors.toList());
    }

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
}
