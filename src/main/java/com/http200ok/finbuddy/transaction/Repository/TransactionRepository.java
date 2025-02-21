package com.http200ok.finbuddy.transaction.Repository;

import com.http200ok.finbuddy.transaction.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
        SELECT t FROM Transaction t 
        JOIN t.account a 
        JOIN a.member m 
        WHERE m.id = :memberId 
        AND a.accountType = 'CHECKING' 
        ORDER BY t.transactionDate DESC
    """)
    List<Transaction> findLatestTransactionsForUserCheckingAccounts(@Param("memberId") Long memberId);
}
