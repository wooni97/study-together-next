package dev.flab.studytogether.infra.repository.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.flab.studytogether.core.domain.notification.ScheduledNotification;
import dev.flab.studytogether.core.domain.notification.repository.ScheduledNotificationRepository;
import dev.flab.studytogether.infra.mapper.NotificationDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ScheduledNotificationRepositoryImpl implements ScheduledNotificationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NotificationDataMapper notificationMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void save(ScheduledNotification scheduledNotification) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("SCHEDULED_NOTIFICATION").usingGeneratedKeyColumns("id");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String payload = null;

        try {
            payload = objectMapper.writeValueAsString(scheduledNotification.getNotificationData());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("notification_type", scheduledNotification.getNotificationData().getClass().getName());
        parameters.put("payload", payload);
        parameters.put("scheduled_at", scheduledNotification.getScheduledAt());

        jdbcInsert.execute(parameters);
    }

    @Override
    public List<ScheduledNotification> findByScheduledAtBefore(LocalDateTime time) {
        String query = "SELECT notification_type, payload, scheduled_at "
        + "FROM SCHEDULED_NOTIFICATION "
        + "WHERE scheduled_at < ?";

        return jdbcTemplate.query(query, scheduledNotificationRowMapper(), time);
    }

    @Override
    public void deleteProcessedScheduledNotifications(List<Long> ids) {
        if(ids.isEmpty()) return;

        String query = "DELETE FROM SCHEDULED_NOTIFICATION WHERE ID IN (:ids)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);

        namedParameterJdbcTemplate.update(query, parameters);
    }

    private RowMapper<ScheduledNotification> scheduledNotificationRowMapper() {
        return (rs, rowNum) -> new ScheduledNotification(
                rs.getLong("id"),
                notificationMapper.dataOf(rs.getString("payload"), rs.getString("notification_type")),
                rs.getTimestamp("scheduled_at").toLocalDateTime());
    }

}
