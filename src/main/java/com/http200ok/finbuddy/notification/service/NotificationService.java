package com.http200ok.finbuddy.notification.service;

import com.http200ok.finbuddy.member.domain.Member;
import com.http200ok.finbuddy.notification.domain.NotificationType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    SseEmitter subscribe(String memberId);
    void sendNotification(Member member, NotificationType notificationType, String content);
}
