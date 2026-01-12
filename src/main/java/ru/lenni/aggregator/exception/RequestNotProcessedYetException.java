package ru.lenni.aggregator.exception;

import java.util.UUID;

public class RequestNotProcessedYetException extends RuntimeException {

    public RequestNotProcessedYetException(UUID taskUid) {
        super(String.format("Request %s not processed yet", taskUid));
    }
}
