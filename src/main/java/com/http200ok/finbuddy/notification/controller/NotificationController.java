package com.http200ok.finbuddy.notification.controller;

import com.http200ok.finbuddy.notification.dto.NotificationResponseDto;
import com.http200ok.finbuddy.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // SSE 연결
    @GetMapping(value = "/subscribe/{memberId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
            @PathVariable("memberId") String memberId,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(memberId, lastEventId);
    }

    // 알림 목록 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<List<NotificationResponseDto>> getNotifications(@PathVariable("memberId") String memberId) {
        List<NotificationResponseDto> notifications = notificationService.getNotifications(memberId);
        return ResponseEntity.ok(notifications);
    }

    // 알림 읽음 표시
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable("notificationId") Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    // 알림 단일 삭제
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable("notificationId") Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok().build();
    }

    // 사용자의 모든 알림 삭제
    @DeleteMapping("/member/{memberId}")
    public ResponseEntity<Void> deleteAllNotifications(@PathVariable("memberId") String memberId) {
        notificationService.deleteAllNotifications(memberId);
        return ResponseEntity.ok().build();
    }

    // 읽지 않은 알림 개수 조회
    @GetMapping("/unread-count/{memberId}")
    public ResponseEntity<Long> getUnreadCount(@PathVariable("memberId") String memberId) {
        Long count = notificationService.getUnreadCount(memberId);
        return ResponseEntity.ok(count);
    }
}