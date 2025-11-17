package ru.lenni.aggregator.dto;

import ru.lenni.aggregator.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record TaskMetadata(UUID taskUid, String type, String filePath,
                           LocalDateTime creationTime, LocalDateTime lastUpdateTime, String status) {
}
