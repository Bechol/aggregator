package ru.lenni.aggregator.exception;

import java.util.UUID;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(UUID taskUid) {
        super(String.format("Task not found by id: %s", taskUid));
    }
}
