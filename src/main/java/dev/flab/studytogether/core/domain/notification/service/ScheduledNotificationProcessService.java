package dev.flab.studytogether.core.domain.notification.service;

import dev.flab.studytogether.core.domain.notification.AbstractNotificationData;
import dev.flab.studytogether.core.domain.notification.ScheduledNotification;
import dev.flab.studytogether.core.domain.notification.repository.ScheduledNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledNotificationProcessService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ScheduledNotificationRepository scheduledNotificationRepository;
    private final NotificationService notificationService;

    @Scheduled(fixedRate = 60000)
    public void process() {
        List<ScheduledNotification> scheduledNotifications =
                scheduledNotificationRepository.findByScheduledAtBefore(LocalDateTime.now());

        List<Long> ids = new ArrayList<>();
        for (ScheduledNotification scheduledNotification : scheduledNotifications) {
            AbstractNotificationData notificationData = scheduledNotification.getNotificationData();
            try {
                notificationService.send(notificationData);
                ids.add(scheduledNotification.getId());
            } catch (RuntimeException e) {
                log.error("scheduled notification id : {} message ; {}", scheduledNotification.getId(), e.getMessage());
            }
        }

        scheduledNotificationRepository.deleteProcessedScheduledNotifications(ids);
    }

}
