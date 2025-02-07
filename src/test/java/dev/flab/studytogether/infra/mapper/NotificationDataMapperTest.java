package dev.flab.studytogether.infra.mapper;

import dev.flab.studytogether.core.domain.notification.AbstractNotificationData;
import dev.flab.studytogether.core.domain.notification.MailNotificationData;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationDataMapperTest {

    private final NotificationDataMapper notificationDataMapper;

    public NotificationDataMapperTest() {
        this.notificationDataMapper = new NotificationDataMapper();
    }

    @Test
    void mappingTest() {
        String json = "{\n" +
                "    \"notificationMessageContent\": \"This is a test email notification.\",\n" +
                "    \"subject\": \"Welcome to the Service!\",\n" +
                "    \"recipientEmailAddress\": \"test@example.com\"\n" +
                "}";

        String notificationDataType = MailNotificationData.class.getName();

        AbstractNotificationData notificationData = notificationDataMapper.dataOf(json, notificationDataType);

        assertThat(notificationData)
                .isInstanceOf(MailNotificationData.class);
    }
}