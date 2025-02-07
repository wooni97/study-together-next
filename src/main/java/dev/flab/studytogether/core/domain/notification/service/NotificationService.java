package dev.flab.studytogether.core.domain.notification.service;

import dev.flab.studytogether.core.domain.notification.AbstractNotificationData;
import dev.flab.studytogether.core.domain.notification.exception.UnsupportedNotificationDataException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class NotificationService {
    private final List<NotificationSender> notificationSenders;
    private final Map<Class<? extends AbstractNotificationData>, NotificationSender> senderMap = new HashMap<>();

    public NotificationService(List<NotificationSender> notificationSenders) {
        this.notificationSenders = List.copyOf(notificationSenders);
        initializeSenderMap();
    }

    private void initializeSenderMap() {
        for (NotificationSender notificationSender : notificationSenders) {
            senderMap.put(notificationSender.getSupportedDataTypeClass(), notificationSender);
        }
    }

    public void send(AbstractNotificationData notificationData) {
        NotificationSender notificationSender = senderMap.get(notificationData.getClass());

        if (notificationSender == null) {
            throw new UnsupportedNotificationDataException(
                    "No sender found for notification data type: " + notificationData.getClass().getName()
            );
        }

        notificationSender.send(notificationData);
    }
}
