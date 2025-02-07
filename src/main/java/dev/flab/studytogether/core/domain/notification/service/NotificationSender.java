package dev.flab.studytogether.core.domain.notification.service;

import dev.flab.studytogether.core.domain.notification.AbstractNotificationData;

public abstract class NotificationSender {

    public abstract Class<? extends AbstractNotificationData> getSupportedDataTypeClass();

    public abstract void send(AbstractNotificationData notificationData);
}
