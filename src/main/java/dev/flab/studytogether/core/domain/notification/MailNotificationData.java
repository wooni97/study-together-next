package dev.flab.studytogether.core.domain.notification;

import lombok.Getter;

@Getter
public class MailNotificationData extends AbstractNotificationData {

    private final String subject;
    private final String recipientEmailAddress;

    public MailNotificationData(String notificationMessageContent,
                                Long memberId,
                                String subject,
                                String recipientEmailAddress) {
        super(notificationMessageContent, memberId);
        this.subject = subject;
        this.recipientEmailAddress = recipientEmailAddress;
    }
}
