package com.http200ok.finbuddy.notification.service;

import com.http200ok.finbuddy.member.domain.Member;
import com.http200ok.finbuddy.notification.domain.NotificationType;
import com.http200ok.finbuddy.notification.dto.NotificationResponseDto;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface NotificationService {
    SseEmitter subscribe(String memberId, String lastEventId);
    void sendNotification(Member member, NotificationType notificationType, String content);
    List<NotificationResponseDto> getNotifications(String memberId);
    void markAsRead(Long notificationId);
    void deleteNotification(Long notificationId);
    void deleteAllNotifications(String memberId);
    Long getUnreadCount(String memberId);
}
