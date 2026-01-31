package ru.lenni.aggregator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.lenni.aggregator.utils.Constants;

import java.util.UUID;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LlmRequestDto {

    @NotNull(message = Constants.REQUEST_ID_CAN_T_BE_NULL)
    private UUID requestId;

    @NotNull(message = Constants.REQUEST_TYPE_CAN_T_BE_NULL)
    private RequestType requestType;

    private String s3Key;
}
