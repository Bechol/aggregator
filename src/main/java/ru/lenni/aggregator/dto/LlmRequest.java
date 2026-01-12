package ru.lenni.aggregator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LlmRequest {

    private UUID requestId;
    private String requestType;
    private String s3Key;
}
