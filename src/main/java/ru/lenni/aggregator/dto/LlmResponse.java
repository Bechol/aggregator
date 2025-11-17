package ru.lenni.aggregator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LlmResponse {

    private UUID taskUid;
    private String taskType;
    private Boolean containsPD;
    private String resultPath;

}
