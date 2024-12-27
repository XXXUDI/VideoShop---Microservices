package com.socompany.userservice.config;

import java.util.HashMap;
import java.util.Map;

import com.socompany.userservice.util.KafkaEnvironment;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.socompany.core.event.UserCreatedEvent;
import com.socompany.core.event.UserUpdatedEvent;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    @Autowired
    private final KafkaEnvironment kafkaEnvironment;

    private final String userCreatedTopic = "user-created-events-topic";

    private final String userUpdatedTopic = "user-updated-events-topic";

    private final short partitions = 3;

    private final short replication = 3;

    @Bean
    NewTopic createUserCreatedTopic() {
        return TopicBuilder
                .name(userCreatedTopic)
                .partitions(partitions)
                .replicas(replication)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }

    @Bean
    NewTopic createUserUpdatedTopic() {
        return TopicBuilder
                .name(userUpdatedTopic)
                .partitions(partitions)
                .replicas(replication)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }




    @Bean
    ProducerFactory<String, UserCreatedEvent> producerUserCreateFactory() {
        // CHECK IS ENVIRONMENT INJECTED IN KAFKA-ENVIRONMENT
        if (!kafkaEnvironment.isInjected(kafkaEnvironment.getProducerProperties())) {
            throw new RuntimeException("The producer properties are not injected");
        }

        return new DefaultKafkaProducerFactory<>(kafkaEnvironment.getProducerProperties());
    }

    @Bean
    KafkaTemplate<String, UserCreatedEvent> kafkaUserCreateTemplate() {
        return new KafkaTemplate<>(producerUserCreateFactory());
    }

    @Bean
    ProducerFactory<String, UserUpdatedEvent> producerUserUpdateFactory() {

        // CHECK IS ENVIRONMENT INJECTED IN KAFKA-ENVIRONMENT
        if (!kafkaEnvironment.isInjected(kafkaEnvironment.getProducerProperties())) {
            throw new RuntimeException("The producer properties are not injected");
        }

        return new DefaultKafkaProducerFactory<>(kafkaEnvironment.getProducerProperties());
    }

    @Bean
    KafkaTemplate<String, UserUpdatedEvent> kafkaUserUpdateTemplate() {
        return new KafkaTemplate<>(producerUserUpdateFactory());
    }

}
