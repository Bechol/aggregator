package ru.lenni.aggregator.exception;

import java.util.UUID;

public class RequestNotFoundException extends RuntimeException {

    public RequestNotFoundException(UUID taskUid) {
        super(String.format("Request not found by id: %s", taskUid));
    }
}
