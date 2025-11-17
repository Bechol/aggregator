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
import org.springframework.web.multipart.MultipartFile;
import ru.lenni.aggregator.dto.TaskMetadata;
import ru.lenni.aggregator.resource.common.TaskType;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.UUID;

@Tag(name = "Task management")
@RequestMapping("/task")
public interface TaskResource {

    @Operation(summary = "Create task for existing file",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Task successfully created",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TaskMetadata.class))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Task file not found",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PostMapping("/{taskType}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<TaskMetadata> createTask(@PathVariable("taskType") TaskType taskType,
                                            @RequestParam("filePath") String filePath);

    @Operation(summary = "Create task with uploadable file",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Task successfully created",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TaskMetadata.class))
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Task file is already exists",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Task file not found",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PostMapping("/{taskType}/upload")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<TaskMetadata> createTask(@PathVariable("taskType") TaskType taskType,
                    @RequestParam("filePath") String filePath,
                    @RequestParam("file") MultipartFile file);

    @Operation(summary = "Find task by id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Task successfully found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TaskMetadata.class))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Task not found",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/{taskUid}")
    ResponseEntity<TaskMetadata> findTaskById(@PathVariable("taskUid") UUID taskUid);

    @Operation(summary = "Get task list",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Task list sorted by last update time in descending order",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = TaskMetadata.class))
                            )
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/list")
    ResponseEntity<List<TaskMetadata>> findAll();

    @Operation(summary = "Task soft delete by id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Task successfully deleted",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @DeleteMapping("/{taskUid}")
    @ResponseStatus(HttpStatus.OK)
    void deleteByUid(@PathVariable("taskUid") UUID taskUid);

    @Operation(summary = "Cancel task processing by id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Task successfully cancelled",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Task not found",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PutMapping("/cancel/{taskUid}")
    @ResponseStatus(HttpStatus.OK)
    void cancelById(@PathVariable("taskUid") UUID taskUid);

    @Operation(summary = "Resend task with SEND_ERROR status by id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Task successfully sent to llm",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Task not found or task already sent",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PutMapping("/resend/{taskUid}")
    @ResponseStatus(HttpStatus.OK)
    void resend(@PathVariable("taskUid") UUID taskUid);

    @Operation(summary = "Download task result by id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Download result file",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Task not processed yet",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/result/{taskUid}")
    ResponseEntity<Resource> downloadResult(@PathVariable("taskUid") UUID taskUid);
}
