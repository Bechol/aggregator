package ru.lenni.aggregator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final LlmKafka llm;

    @Data
    public static class LlmKafka {

        private String requestTopic;
        private String responseTopic;
    }
}
