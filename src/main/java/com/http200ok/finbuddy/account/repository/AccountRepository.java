package com.http200ok.finbuddy.account.repository;

import com.http200ok.finbuddy.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT DISTINCT a FROM Account a " +
            "LEFT JOIN FETCH a.transactions t " +
            "LEFT JOIN FETCH t.category c " +
            "WHERE a.id = :accountId " +
            "AND a.member.id = :memberId")
    Optional<Account> findAccountWithTransactions(
            @Param("accountId") Long accountId,
            @Param("memberId") Long memberId
    );

    @Query("""
        SELECT a FROM Account a 
        WHERE a.member.id = :memberId 
        AND a.accountType = 'CHECKING' 
        ORDER BY a.id
    """)
    List<Account> findCheckingAccountsByMemberId(@Param("memberId") Long memberId);

}

