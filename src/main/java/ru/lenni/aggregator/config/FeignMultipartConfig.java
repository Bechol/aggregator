package ru.lenni.aggregator.config;

import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;

@Slf4j
public class FeignMultipartConfig {

    private final ObjectFactory<HttpMessageConverters> messageConverters;

    public FeignMultipartConfig(ObjectFactory<HttpMessageConverters> messageConverters) {
        this.messageConverters = messageConverters;
    }

    @Bean
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            log.error("Feign client error: method={}, status={}, reason={}",
                    methodKey, response.status(), response.reason());
            return new RuntimeException(
                    String.format("Feign error: %s - %s", response.status(), response.reason())
            );
        };
    }
}
