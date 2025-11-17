package ru.lenni.aggregator.exception;

import java.util.UUID;

public class LlmSendException extends RuntimeException {

    public LlmSendException(UUID taskUuid, Throwable throwable) {
        super(
           String.format("Failed to send message to llm for task id %s.", taskUuid),
                throwable
        );
    }
}
