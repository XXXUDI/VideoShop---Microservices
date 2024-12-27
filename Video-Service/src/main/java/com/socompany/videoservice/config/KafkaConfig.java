package com.socompany.videoservice.config;

import com.socompany.core.event.VideoEvent;
import com.socompany.videoservice.util.KafkaEnvironment;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Configuration
public class KafkaConfig {

    // Topics
    private final String videoPublishedTopic = "video-published-events-topic";
    private final String videoUnpublishedTopic = "video-unpublished-events-topic";
    private final String videoPurchasedTopic = "video-purchased-events-topic";

    // Other kafka broker configs

    private final short replication = 3;
    private final short partitions = 3;


    private Map<String, Object> kafkaProducerConfig;

    public KafkaConfig(KafkaEnvironment kafkaEnvironment) {
        if(kafkaEnvironment.isInjected(kafkaEnvironment.getProducerProperties())){
            kafkaProducerConfig = kafkaEnvironment.getProducerProperties();
        } else {
            kafkaEnvironment.printAllProperties();
            throw new RuntimeException("Kafka Environment is not injected");
        }
    }

    // Creating topic for published video
    @Bean
    public NewTopic videoPublishedEvents() {
        return TopicBuilder
                .name(videoPublishedTopic)
                .partitions(partitions)
                .replicas(replication)
                .build();
    }

    @Bean
    public NewTopic videoUnpublishedEvents() {
        return TopicBuilder
                .name(videoUnpublishedTopic)
                .partitions(partitions)
                .replicas(replication)
                .build();
    }

    @Bean
    public NewTopic videoPurchasedEvents() {
        return TopicBuilder
                .name(videoPurchasedTopic)
                .partitions(partitions)
                .replicas(replication)
                .build();
    }



    // TRYING TO USE SINGLE EVENT FOR ALL TOPICS

    @Bean
    ProducerFactory<String, VideoEvent> producerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProducerConfig);
    }

    @Bean
    public KafkaTemplate<String, VideoEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
