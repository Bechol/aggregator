package ru.lenni.aggregator.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.lenni.aggregator.dto.LlmResponse;
import ru.lenni.aggregator.service.TaskService;

@Component
@RequiredArgsConstructor
public class TaskResponseConsumer {

    private final TaskService taskService;

    @KafkaListener(topics = "${app.llm.response-topic:llm-response}", containerFactory = "llmResponseContainerFactory")
    public void listener(LlmResponse response) {
        taskService.processLlmResult(response);
    }
}
