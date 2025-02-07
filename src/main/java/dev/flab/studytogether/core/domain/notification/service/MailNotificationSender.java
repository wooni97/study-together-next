package dev.flab.studytogether.core.domain.notification.service;

import dev.flab.studytogether.core.domain.notification.AbstractNotificationData;
import dev.flab.studytogether.core.domain.notification.MailNotificationData;
import dev.flab.studytogether.infra.mail.MailClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class MailNotificationSender extends NotificationSender {

    private final MailClient mailClient;

    @Override
    public Class<? extends AbstractNotificationData> getSupportedDataTypeClass() {
        return MailNotificationData.class;
    }

    @Override
    public void send(AbstractNotificationData notificationData) {
        if (!(notificationData instanceof MailNotificationData mailNotification))
            throw new ClassCastException("Expected MailNotification, but received: " + notificationData.getClass().getSimpleName());

        mailClient.sendMail(mailNotification.getSubject(),
                mailNotification.getNotificationMessageContent(),
                mailNotification.getRecipientEmailAddress());
    }
}
