package ru.lenni.aggregator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lenni.aggregator.config.AppProperties;
import ru.lenni.aggregator.dto.LlmRequestDto;
import ru.lenni.aggregator.dto.RequestStatus;
import ru.lenni.aggregator.exception.LlmSendException;
import ru.lenni.aggregator.model.Request;
import ru.lenni.aggregator.repository.RequestRepository;
import ru.lenni.aggregator.service.LlmSenderService;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LlmSenderServiceImpl implements LlmSenderService {

    private final AppProperties props;
    private final RequestRepository requestRepository;
    private final KafkaTemplate<UUID, LlmRequestDto> llmRequestKafkaTemplate;

    @Override
    @Transactional
    public void sendRequestToLlm(Request request) {
        var record = createKafkaMessage(request);
        llmRequestKafkaTemplate.send(record)
                .whenCompleteAsync((result, exception) -> {
                    if (Objects.isNull(exception) && !RequestStatus.CANCELLED.equals(request.getStatus())) {
                        requestRepository.save(request.setStatus(RequestStatus.IN_PROGRESS));
                    }
                })
                .exceptionallyAsync(throwable -> {
                    requestRepository.save(request.setStatus(RequestStatus.SEND_ERROR));
                    throw new LlmSendException(request.getId(), throwable);
                });
    }

    @Override
    @Transactional
    public void resendErrorTask(Request request) {
        var record = createKafkaMessage(request);
        llmRequestKafkaTemplate.send(record).whenCompleteAsync((result, exception) -> {
            if (Objects.isNull(exception)) {
                requestRepository.save(request.setStatus(RequestStatus.IN_PROGRESS));
            }
        });
    }

    private ProducerRecord<UUID, LlmRequestDto> createKafkaMessage(Request request) {
        LlmRequestDto messageData = new LlmRequestDto()
                .setRequestId(request.getId())
                .setRequestType(request.getType())
                .setS3Key(request.getS3Key());
        return new ProducerRecord<>(
                props.getLlm().getRequestTopic(),
                messageData.getRequestId(),
                messageData
        );
    }

}
