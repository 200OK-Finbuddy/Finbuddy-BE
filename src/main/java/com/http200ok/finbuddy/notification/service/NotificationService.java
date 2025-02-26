package com.http200ok.finbuddy.notification.service;

import com.http200ok.finbuddy.budget.domain.Budget;
import com.http200ok.finbuddy.member.domain.Member;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    SseEmitter subscribe(String memberId);
    void sendBudgetExceededNotification(Member member, Budget budget, String content);
}
