package com.socompany.notificationservice.handler;



import com.socompany.core.event.UserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@KafkaListener(topics = "user-created-events-topic")
public class UserCreatedEventHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(UserCreatedEventHandler.class);

    @KafkaHandler
    public void handle(@Payload UserCreatedEvent userCreatedEvent,
                       @Header(name= "message ID") String messageId) {

        try {
            LOGGER.info("Received user created event: {}", userCreatedEvent);
            // Логика обработки события
        } catch (Exception e) {
            LOGGER.error("Error processing event: {}", userCreatedEvent, e);
            throw e; // Исключение приведет к отправке в DLT
        }
    }

}
