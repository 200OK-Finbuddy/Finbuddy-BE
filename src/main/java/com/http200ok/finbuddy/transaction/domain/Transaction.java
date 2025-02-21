package com.http200ok.finbuddy.transaction.domain;

import com.http200ok.finbuddy.account.domain.Account;
import com.http200ok.finbuddy.category.domain.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private String opponentName;

    @Column(nullable = false)
    private Integer transactionType; // 입금(1) or 출금(2)

    @Column(nullable = false)
    private Long amount;

    private Long updatedBalance;

    private LocalDateTime transactionDate;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
