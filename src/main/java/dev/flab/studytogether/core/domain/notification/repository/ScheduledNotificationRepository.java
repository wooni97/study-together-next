package dev.flab.studytogether.core.domain.notification.repository;

import dev.flab.studytogether.core.domain.notification.ScheduledNotification;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledNotificationRepository {
    void save(ScheduledNotification scheduledNotification);
    List<ScheduledNotification> findByScheduledAtBefore(LocalDateTime time);
    void deleteProcessedScheduledNotifications(List<Long> ids);
}
