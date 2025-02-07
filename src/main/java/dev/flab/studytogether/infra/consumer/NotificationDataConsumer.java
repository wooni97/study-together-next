package dev.flab.studytogether.infra.consumer;

import dev.flab.studytogether.core.domain.notification.AbstractNotificationData;
import dev.flab.studytogether.infra.mapper.NotificationDataMapper;
import dev.flab.studytogether.core.domain.notification.service.NotificationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationDataConsumer {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final JdbcTemplate jdbcTemplate;
    private final NotificationDataMapper notificationMapper;
    private final NotificationService notificationService;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Scheduled(fixedDelay = 5000)
    public void consume() {
        List<NotificationDto> notificationDtos = findAll();
        List<Long> ids = new ArrayList<>();

        for(NotificationDto notificationDto : notificationDtos) {
            AbstractNotificationData notificationData = notificationDto.getNotificationData();
            try {
                notificationService.send(notificationData);
                ids.add(notificationDto.getId());
            } catch (RuntimeException e) {
                log.error("notification id : {} messae ; {}", notificationDto.getId(), e.getMessage());

            }
        }

        deleteProcessedNotifications(ids);
    }

    private void deleteProcessedNotifications(List<Long> processedNotificationIds) {
        if(processedNotificationIds.isEmpty()) return;

        String query = "DELETE FROM NOTIFICATION WHERE ID IN (:ids)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", processedNotificationIds);

        namedParameterJdbcTemplate.update(query, parameters);
    }

    public List<NotificationDto> findAll() {
        String query = "SELECT ID, NOTIFICATION_TYPE, PAYLOAD FROM NOTIFICATION";
        return jdbcTemplate.query(query, notificationDtoRowMapper());
    }

    private RowMapper<NotificationDto> notificationDtoRowMapper() {
        return ((rs, rowNum) -> new NotificationDto(
                rs.getLong("ID"),
                notificationMapper.dataOf(rs.getString("PAYLOD"), rs.getString("NOTIFICATION_TYPE"))
        ));
    }

    @Getter
    @RequiredArgsConstructor
    public static class NotificationDto {

        private final Long id;
        private final AbstractNotificationData notificationData;
    }
}
