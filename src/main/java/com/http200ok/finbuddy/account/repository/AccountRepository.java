package com.http200ok.finbuddy.account.repository;

import com.http200ok.finbuddy.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("""
        SELECT a FROM Account a 
        WHERE a.member.id = :memberId 
        ORDER BY a.id
    """)
    List<Account> findAccountsByMemberId(@Param("memberId") Long memberId);

    @Query("""
        SELECT a FROM Account a 
        WHERE a.member.id = :memberId 
        AND a.accountType = 'CHECKING' 
        ORDER BY a.id
    """)
    List<Account> findCheckingAccountsByMemberId(@Param("memberId") Long memberId);


    // 계좌번호로 계좌 조회
    Optional<Account> findByAccountNumber(String accountNumber);
}

