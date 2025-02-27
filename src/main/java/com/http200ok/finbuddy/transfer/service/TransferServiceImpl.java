package com.http200ok.finbuddy.transfer.service;

import com.http200ok.finbuddy.account.domain.Account;
import com.http200ok.finbuddy.account.repository.AccountRepository;
import com.http200ok.finbuddy.budget.service.BudgetService;
import com.http200ok.finbuddy.category.domain.Category;
import com.http200ok.finbuddy.category.repository.CategoryRepository;
import com.http200ok.finbuddy.notification.service.NotificationService;
import com.http200ok.finbuddy.transaction.domain.Transaction;
import com.http200ok.finbuddy.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final BudgetService budgetService;
    private final NotificationService notificationService;

    /**
     * 계좌 이체 처리
     * @param fromAccountNumber 출금 계좌번호
     * @param toAccountNumber 입금 계좌번호
     * @param amount 이체 금액
     * @param password 계좌 비밀번호
     * @param senderName 보내는 사람 이름, 받는 분 통장에 표시
     * @param receiverName 받는 사람 이름, 내 통상에 표시
     * @return 이체 성공 여부
     */
    @Override
    @Transactional
    public boolean transferMoney(Long memberId, String fromAccountNumber, String toAccountNumber,
                                 Long amount, String password, String senderName, String receiverName) {

        // 계좌 번호 유효성 검사
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException("출금계좌와 입금계좌가 동일합니다");
        }

        // 계좌 조회 - 비관적 락 사용(동시성 제어)
        Account fromAccount = accountRepository.findByAccountNumberWithPessimisticLock(fromAccountNumber)
                .orElseThrow(() -> new RuntimeException("출금 계좌를 찾을 수 없습니다"));

        Account toAccount = accountRepository.findByAccountNumberWithPessimisticLock(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("입금 계좌를 찾을 수 없습니다"));

        // 비밀번호 검증
        if (!fromAccount.getPassword().equals(password)) {
            throw new RuntimeException("계좌 비밀번호가 일치하지 않습니다");
        }

        // 잔액 확인
        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException("잔액이 부족합니다");
        }

        // 거래 카테고리 조회 (이체 카테고리 - 실제 코드에서는 상수로 관리하거나 DB에서 조회)
        Category transferCategory = categoryRepository.findById(7L) // 기타로 우선 저장
                .orElseThrow(() -> new RuntimeException("거래 카테고리를 찾을 수 없습니다"));

        LocalDateTime now = LocalDateTime.now();

        // 출금 계좌 업데이트
        fromAccount.setBalance(fromAccount.getBalance() - amount);

        // 입금 계좌 업데이트
        toAccount.setBalance(toAccount.getBalance() + amount);

        // 출금 거래내역 생성, 출금(2)
        Transaction withdrawalTransaction = Transaction.createTransaction(fromAccount, receiverName, amount, 2, transferCategory);

        // 입금 거래내역 생성, 입금(1)
        Transaction depositTransaction = Transaction.createTransaction(toAccount, senderName, amount, 1, transferCategory);

        // 거래내역 저장
        transactionRepository.save(withdrawalTransaction);
        transactionRepository.save(depositTransaction);

        // 계좌 정보 업데이트
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // checkAndNotifyBudgetExceededOnTransaction(memberId);

        return true;
    }

    // 이체 발생 시 즉시 예산 초과 여부 확인 및 알림 전송
    @Override
    public void checkAndNotifyBudgetExceededOnTransaction(Long memberId) {
        budgetService.getCurrentMonthBudget(memberId)
                .ifPresent(budget -> {
                    Long totalSpending = transactionRepository.getTotalSpendingForCurrentMonth(memberId);
                    if (totalSpending > budget.getBudget()) {
                        notificationService.sendBudgetExceededNotification(
                                budget.getMember(), budget, "예산을 초과하였습니다."
                        );
                        System.out.println("예산초과");
                    } else {
                        System.out.println("예산이하");
                    }
                });
    }

}
