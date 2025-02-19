package com.http200ok.finbuddy.notification.domain;

import com.http200ok.finbuddy.budget.domain.Budget;
import com.http200ok.finbuddy.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;

    @Column(nullable = false)
    private String message;

    private Boolean isRead;
    private LocalDateTime createdAt;
}
