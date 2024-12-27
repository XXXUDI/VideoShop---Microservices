package com.socompany.userservice.service;

import com.socompany.core.event.UserCreatedEvent;
import com.socompany.userservice.dto.UserRequestDto;
import com.socompany.userservice.util.UserCreationException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@DirtiesContext
@EmbeddedKafka(controlledShutdown = true)
@SpringBootTest(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}"})
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    EmbeddedKafkaBroker embeddedKafka;

    @Autowired
    Environment env;

    private KafkaMessageListenerContainer<String, UserCreatedEvent> container;

    private BlockingQueue<ConsumerRecord<String, UserCreatedEvent>> records;

    private final String topic = "user-created-events-topic";

    @BeforeAll
    public void setup() {
        DefaultKafkaConsumerFactory<String, Object> defaultKafkaConsumerFactory =
                new DefaultKafkaConsumerFactory<>(getConsumerProperties());

        ContainerProperties containerProperties = new ContainerProperties(topic);

        // Container Initialization
        container = new KafkaMessageListenerContainer<>(defaultKafkaConsumerFactory, containerProperties);

        records = new LinkedBlockingQueue<>();

        container.setupMessageListener((MessageListener<String, UserCreatedEvent>) records::add);

        container.start();

        ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());

    }

    private Map<String, Object> getConsumerProperties() {
        return Map.of(
                // Switch brokers to embedded kafka
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafka.getBrokersAsString(),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class,
                ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class,
                ConsumerConfig.GROUP_ID_CONFIG, env.getProperty("spring.kafka.consumer.group-id"),
                JsonDeserializer.TRUSTED_PACKAGES, env.getProperty("spring.kafka.consumer.properties.spring.json.trusted.packages"),
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, env.getProperty("spring.kafka.consumer.auto-offset-reset")
        );
    }

    @AfterAll
    public void stop() {
        container.stop();
    }

    @Test
    public void createUser_withValidData_successfullySendToKafka() throws ExecutionException, InterruptedException, UserCreationException {
        // ARRANGE
        UserRequestDto requestDto = UserRequestDto.builder()
                .username("Markus12")
                .email("markus12@gmail.com")
                .password("xx.21!&11xxxx")
                .build();

        // ACT
        var actual = userService.createUser(requestDto);

        // ASSERT

        ConsumerRecord<String, UserCreatedEvent> record = records.poll(3000, TimeUnit.MILLISECONDS);

        assertNotNull(record);
        assertNotNull(record.key());

        UserCreatedEvent userCreatedEvent = record.value();

        assertEquals(requestDto.getUsername(), userCreatedEvent.getUsername());
        assertEquals(requestDto.getEmail(), userCreatedEvent.getEmail());
        assertEquals(requestDto.getPassword(), userCreatedEvent.getPassword());

        assertNotNull(actual);

        assertEquals(actual.getUsername(), requestDto.getUsername());
        assertEquals(actual.getEmail(), requestDto.getEmail());
        assertEquals(actual.getPassword(), requestDto.getPassword());

    }


    // TEST IS PASSED
}


