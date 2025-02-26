package com.http200ok.finbuddy.notification.service;

import com.http200ok.finbuddy.budget.domain.Budget;
import com.http200ok.finbuddy.member.domain.Member;
import com.http200ok.finbuddy.notification.domain.Notification;
import com.http200ok.finbuddy.notification.repository.EmitterRepository;
import com.http200ok.finbuddy.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{

    private static final Long DEFAULT_TIMEOUT = 60L * 1000L * 60; // 1시간
    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    // 알림 구독 요청 시 호출됨
    public SseEmitter subscribe(String memberId) {
        String emitterId = memberId + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
        emitter.onError((e) -> emitterRepository.deleteById(emitterId));

        // 연결 직후 더미 이벤트 전송(503 방지)
        try {
            emitter.send(SseEmitter.event().name("connect").data("Connected!"));
        } catch (Exception e) {
            emitterRepository.deleteById(emitterId);
        }

        return emitter;
    }

    // 예산 초과 시 알림을 보내는 메서드
    public void sendBudgetExceededNotification(Member member, Budget budget, String content) {
        Notification notification = Notification.builder()
                .member(member)
                .budget(budget)
                .content(content)
                .build();

        notificationRepository.save(notification);
        sendToClient(member.getId().toString(), notification);
    }

    // 클라이언트에게 이벤트를 전송
    private void sendToClient(String memberId, Notification notification) {
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByMemberId(memberId);
        sseEmitters.forEach((key, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("budgetExceeded")
                        .data(notification.getContent()));
            } catch (Exception e) {
                emitterRepository.deleteById(key);
            }
        });
    }
}
