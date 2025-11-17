package ru.lenni.aggregator.exception;

import java.util.UUID;

public class TaskNotProcessedYetException extends RuntimeException {

    public TaskNotProcessedYetException(UUID taskUid) {
        super(String.format("Task %s not processed yet", taskUid));
    }
}
