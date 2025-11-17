package ru.lenni.aggregator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lenni.aggregator.config.AppProperties;
import ru.lenni.aggregator.dto.LlmRequest;
import ru.lenni.aggregator.exception.LlmSendException;
import ru.lenni.aggregator.model.Task;
import ru.lenni.aggregator.model.TaskStatus;
import ru.lenni.aggregator.repository.TaskRepository;
import ru.lenni.aggregator.service.LlmSenderService;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LlmSenderServiceImpl implements LlmSenderService {

    private final AppProperties props;
    private final TaskRepository taskRepository;
    private final KafkaTemplate<UUID, LlmRequest> llmRequestKafkaTemplate;

    @Override
    @Transactional
    public void sendRequestToLlm(Task task) {
        var record = createKafkaMessage(task);
        llmRequestKafkaTemplate.send(record)
                .whenCompleteAsync((result, exception) -> {
                    if (Objects.isNull(exception) && !TaskStatus.CANCELLED.equals(task.getStatus())) {
                        taskRepository.save(task.setStatus(TaskStatus.IN_PROGRESS));
                    }
                })
                .exceptionallyAsync(throwable -> {
                    taskRepository.save(task.setStatus(TaskStatus.SEND_ERROR));
                    throw new LlmSendException(task.getId(), throwable);
                });
    }

    @Override
    @Transactional
    public void resendErrorTask(Task task) {
        var record = createKafkaMessage(task);
        llmRequestKafkaTemplate.send(record).whenCompleteAsync((result, exception) -> {
            if (Objects.isNull(exception)) {
                taskRepository.save(task.setStatus(TaskStatus.IN_PROGRESS));
            }
        });
    }

    private ProducerRecord<UUID, LlmRequest> createKafkaMessage(Task task) {
        LlmRequest request = new LlmRequest()
                .setTaskUid(task.getId())
                .setTaskType(task.getTaskType().name())
                .setStatus(task.getStatus().name())
                .setFilePath(task.getFilePath());
        return new ProducerRecord<>(
                props.getLlm().getRequestTopic(),
                task.getId(),
                request
        );
    }

}
