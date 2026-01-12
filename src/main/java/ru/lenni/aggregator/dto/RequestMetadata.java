package ru.lenni.aggregator.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record RequestMetadata(
        UUID requestUid, String type, LocalDateTime creationTime, LocalDateTime lastUpdateTime, String status) {
}
