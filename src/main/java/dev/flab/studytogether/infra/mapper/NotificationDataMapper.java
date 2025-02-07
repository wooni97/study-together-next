package dev.flab.studytogether.infra.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.flab.studytogether.core.domain.notification.AbstractNotificationData;
import dev.flab.studytogether.infra.mapper.rowmapper.NotificationDataRowMapper;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class NotificationDataMapper {

    private Map<Class<? extends AbstractNotificationData>, Class<? extends NotificationDataRowMapper>> typeMap = new HashMap<>();

    public NotificationDataMapper() {
        this("dev.flab.studytogether");
    }

    private NotificationDataMapper(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<? extends AbstractNotificationData>> notificationDataClasses = reflections.getSubTypesOf(AbstractNotificationData.class);
        Set<Class<? extends NotificationDataRowMapper>> rowClasses = reflections.getSubTypesOf(NotificationDataRowMapper.class);

        typeMap = rowClasses
                .stream()
                .collect(Collectors.toMap(rowClass -> {
                    try {
                        return rowClass.getDeclaredConstructor().newInstance().notificationDataType();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }}, rowClass -> rowClass));

        if(!typeMap.keySet().containsAll(notificationDataClasses))
            throw new RuntimeException(new ClassNotFoundException());
    }

    public AbstractNotificationData dataOf(String json, String notificationDataTypeValue) {
        Class<?> clazz;
        try {
            clazz = Class.forName(notificationDataTypeValue);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        if (!AbstractNotificationData.class.isAssignableFrom(clazz))
            throw new ClassCastException(notificationDataTypeValue + " is not a subclass of AbstractNotificationData");

        Class<? extends AbstractNotificationData> notificationDataType = clazz.asSubclass(AbstractNotificationData.class);
        Class<? extends NotificationDataRowMapper> rowMapperType = rowMapperFrom(notificationDataType);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, rowMapperType).createNotificationDataDomain();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Class<? extends NotificationDataRowMapper> rowMapperFrom(Class<? extends AbstractNotificationData> notificationDataType) {
        Class<? extends NotificationDataRowMapper> type = typeMap.get(notificationDataType);
        if(type == null) throw new RuntimeException(new ClassNotFoundException("not found class : " + notificationDataType));
        return type;
    }


}
