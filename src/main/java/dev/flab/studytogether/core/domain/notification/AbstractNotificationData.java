package dev.flab.studytogether.core.domain.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public abstract class AbstractNotificationData {

    private final String notificationMessageContent;
    private final Long notifiedMemberId;
}
