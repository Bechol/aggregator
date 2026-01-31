package ru.lenni.aggregator.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lenni.aggregator.dto.RequestMetadata;

import java.util.List;
import java.util.UUID;

@Tag(name = "Request management")
@RequestMapping("/task")
public interface TaskResource {

    @Operation(summary = "Get task list",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Request list sorted by last update time in descending order",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = RequestMetadata.class))
                            )
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/list")
    ResponseEntity<List<RequestMetadata>> findAll();

    @Operation(summary = "Request soft delete by id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Request successfully deleted",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @DeleteMapping("/{requestUid}")
    @ResponseStatus(HttpStatus.OK)
    void deleteByUid(@PathVariable("requestUid") UUID taskUid);

    @Operation(summary = "Cancel task processing by id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Request successfully cancelled",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Request not found",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PutMapping("/cancel/{requestUid}")
    @ResponseStatus(HttpStatus.OK)
    void cancelById(@PathVariable("requestUid") UUID taskUid);

    @Operation(summary = "Resend task with SEND_ERROR status by id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Request successfully sent to llm",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Request not found or task already sent",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PutMapping("/resend/{requestUid}")
    @ResponseStatus(HttpStatus.OK)
    void resend(@PathVariable("requestUid") UUID taskUid);

}
