package com.naturedex.recognition_worker.config;

import com.naturedex.recognition_worker.events.ObservationUploadedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrap;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ObservationUploadedEvent> recognitionKafkaListenerFactory() {

        JsonDeserializer<ObservationUploadedEvent> json = new JsonDeserializer<>(ObservationUploadedEvent.class);
        json.addTrustedPackages("*");

        var cf = new DefaultKafkaConsumerFactory<String, ObservationUploadedEvent>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap,
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                        ConsumerConfig.GROUP_ID_CONFIG, "recognition",
                        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false
                ),
                new StringDeserializer(), json
        );

        var factory = new ConcurrentKafkaListenerContainerFactory<String, ObservationUploadedEvent>();
        factory.setConsumerFactory(cf);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setConcurrency(1);
        return factory;

    }
}
