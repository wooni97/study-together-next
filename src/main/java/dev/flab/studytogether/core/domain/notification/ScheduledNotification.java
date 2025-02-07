package dev.flab.studytogether.core.domain.notification;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduledNotification {

    private Long id;
    private final AbstractNotificationData notificationData;
    private final LocalDateTime scheduledAt;

    public ScheduledNotification(AbstractNotificationData notificationData,
                                 LocalDateTime scheduledAt) {
        this.notificationData = notificationData;
        this.scheduledAt = scheduledAt;
    }

    public ScheduledNotification(Long id,
                                 AbstractNotificationData notificationData,
                                 LocalDateTime scheduledAt) {
        this.id = id;
        this.notificationData = notificationData;
        this.scheduledAt = scheduledAt;
    }
}
