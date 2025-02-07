package dev.flab.studytogether.infra.mapper.rowmapper;

import dev.flab.studytogether.core.domain.notification.AbstractNotificationData;

public interface NotificationDataRowMapper<T extends AbstractNotificationData> {

    T createNotificationDataDomain();
    Class<T> notificationDataType();
}
