package ru.lenni.aggregator.config;

import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.micrometer.KafkaTemplateObservation;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.lenni.aggregator.dto.LlmRequestDto;
import ru.lenni.aggregator.dto.LlmResponseDto;

import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<UUID, LlmRequestDto> taskProducerFactory() {
        var producerProps = kafkaProperties.buildProducerProperties();
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        producerProps.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
        return new DefaultKafkaProducerFactory<>(producerProps);
    }

    @Bean
    public KafkaTemplate<UUID, LlmRequestDto> llmRequestKafkaTemplate(ProducerFactory<UUID, LlmRequestDto> taskProducerFactory,
                                                                   ObservationRegistry observationRegistry) {
        KafkaTemplate<UUID, LlmRequestDto> kafkaTemplate = new KafkaTemplate<>(taskProducerFactory);
        kafkaTemplate.setMicrometerEnabled(true);
        kafkaTemplate.setObservationConvention(new KafkaTemplateObservation.DefaultKafkaTemplateObservationConvention());
        kafkaTemplate.setObservationEnabled(true);
        kafkaTemplate.setObservationRegistry(observationRegistry);
        return kafkaTemplate;
    }

    @Bean
    public ConsumerFactory<UUID, LlmResponseDto> taskConsumerFactory() {
        var consumerProps = kafkaProperties.buildConsumerProperties();
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, LlmResponseDto.class);
        return new DefaultKafkaConsumerFactory<>(consumerProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<UUID, LlmResponseDto> llmResponseContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<UUID, LlmResponseDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(taskConsumerFactory());
        return factory;
    }
}
