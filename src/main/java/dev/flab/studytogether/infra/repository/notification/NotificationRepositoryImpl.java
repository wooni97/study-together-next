package dev.flab.studytogether.infra.repository.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.flab.studytogether.core.domain.notification.AbstractNotificationData;
import dev.flab.studytogether.core.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(AbstractNotificationData notificationData) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("NOTIFICATION").usingGeneratedKeyColumns("ID");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String payload = null;

        try {
            payload = objectMapper.writeValueAsString(notificationData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("notification_type", notificationData.getClass().getName());
        parameters.put("notified_member_id", notificationData.getNotifiedMemberId());
        parameters.put("payload", payload);

        jdbcInsert.execute(parameters);

    }
}
