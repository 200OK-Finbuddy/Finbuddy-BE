package com.http200ok.finbuddy.autotransfer.domain;

import com.http200ok.finbuddy.account.domain.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class AutoTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auto_transfer_id")
    private Long id;

    // 출금 계좌
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // 입금 계좌
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account targetAccount;

    @Column(nullable = false)
    private Long amount;

    // 매월 몇 일에 실행할지
    @Column(nullable = false)
    private Integer transferDay;

    // 자동이체 상태 (ACTIVE, INACTIVE, CANCELLED)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AutoTransferStatus status;

    private LocalDateTime createAt;
    private LocalDateTime updatedAt;
}
