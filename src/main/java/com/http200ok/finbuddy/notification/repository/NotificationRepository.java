package com.http200ok.finbuddy.notification.repository;

import com.http200ok.finbuddy.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
