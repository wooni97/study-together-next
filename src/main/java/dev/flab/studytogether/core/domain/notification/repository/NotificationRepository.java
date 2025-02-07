package dev.flab.studytogether.core.domain.notification.repository;

import dev.flab.studytogether.core.domain.notification.AbstractNotificationData;

public interface NotificationRepository {
    void save(AbstractNotificationData notification);
}
