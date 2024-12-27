package com.socompany.videoservice.util;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaEnvironment {

    private final Logger LOGGER = LoggerFactory.getLogger(KafkaEnvironment.class);

    private final Environment environment;

    @Autowired
    public KafkaEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String getProducerBootsStrapServers() {
        LOGGER.debug("Giving producer bootstrap servers");
        LOGGER.debug("Server: {}", environment.getProperty("spring.kafka.bootstrap-servers"));
        return environment.getProperty("spring.kafka.producer.bootstrap-servers");
    }

    public Map<String, Object> getProducerProperties() {
        Map<String, Object> producerProperties = new HashMap<>();

        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("spring.kafka.producer.bootstrap-servers"));
        producerProperties.put(ProducerConfig.ACKS_CONFIG, environment.getProperty("spring.kafka.producer.acks"));
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        producerProperties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, environment.getProperty("spring.kafka.producer.properties.request.timeout.ms"));
        producerProperties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, environment.getProperty("spring.kafka.producer.properties.request.timeout.ms"));
        return producerProperties;
    }

    public Map<String, Object> getConsumerProperties() {
        Map<String, Object> consumerProperties = new HashMap<>();

        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("spring.kafka.consumer.bootstrap-servers"));
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        consumerProperties.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, environment.getProperty("spring.kafka.consumer.group-id"));
        consumerProperties.put(JsonDeserializer.TRUSTED_PACKAGES, "");

        return consumerProperties;
    }

    public boolean isInjected(Map<String, Object> configMap) {
        boolean allInjected = true;
        for (Map.Entry<String, Object> entry : configMap.entrySet()) {
            if (entry.getValue() == null) {
                LOGGER.debug("{} is null", entry.getKey());
                allInjected = false;
            }
        }
        return allInjected;
    }

    public void printAllProperties() {
        LOGGER.debug("Printing all properties from environment:");
        ((AbstractEnvironment) environment).getPropertySources().forEach(propertySource -> {
            if (propertySource instanceof EnumerablePropertySource<?>) {
                String[] propertyNames = ((EnumerablePropertySource<?>) propertySource).getPropertyNames();
                for (String propertyName : propertyNames) {
                    LOGGER.debug("{}: {}", propertyName, environment.getProperty(propertyName));
                }
            }
        });
    }

}