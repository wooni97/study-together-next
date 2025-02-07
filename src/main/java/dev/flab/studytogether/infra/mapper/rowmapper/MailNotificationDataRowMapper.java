package dev.flab.studytogether.infra.mapper.rowmapper;

import dev.flab.studytogether.core.domain.notification.MailNotificationData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MailNotificationDataRowMapper implements NotificationDataRowMapper<MailNotificationData> {

    private String notificationMessageContent;
    private String subject;
    private String recipientEmailAddress;


    @Override
    public MailNotificationData createNotificationDataDomain() {
        return new MailNotificationData(notificationMessageContent, null, subject, recipientEmailAddress);
    }

    @Override
    public Class<MailNotificationData> notificationDataType() {
        return MailNotificationData.class;
    }
}
