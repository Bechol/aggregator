package ru.lenni.aggregator.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.lenni.aggregator.dto.LlmResponseDto;
import ru.lenni.aggregator.service.RequestService;

@Component
@RequiredArgsConstructor
public class TaskResponseConsumer {

    private final RequestService requestService;

    @KafkaListener(topics = "${app.llm.response-topic:llm-response}", containerFactory = "llmResponseContainerFactory")
    public void listener(LlmResponseDto response) {
        requestService.processLlmResult(response);
    }
}
