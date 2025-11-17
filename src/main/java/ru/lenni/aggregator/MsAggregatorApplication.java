package ru.lenni.aggregator;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.lenni.aggregator.config.AppProperties;

@OpenAPIDefinition(info = @Info(
		title = "MS Aggregator API",
		version = "1.0"
))
@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties(AppProperties.class)
public class MsAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAggregatorApplication.class, args);
	}

}
