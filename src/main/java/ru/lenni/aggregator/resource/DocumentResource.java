package ru.lenni.aggregator.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.lenni.aggregator.dto.TaskMetadata;
import ru.lenni.aggregator.resource.common.TaskType;

import java.util.List;
import java.util.UUID;

@Tag(name = "Операции с документами")
@RequestMapping("/doc")
public interface DocumentResource {

    @Operation(summary = "Действия с загружаемым файлом",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Скачать результат обработки",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PostMapping("/{taskType}/upload")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Resource> processFileWithUpload(@PathVariable("taskType") TaskType taskType,
                    @RequestParam("file") MultipartFile file);

    @Operation(summary = "Действия с файлом по ссылке (S3)",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Скачать результат обработки",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Файл по указанному пути не найден",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PostMapping("/{taskType}/path")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Resource> processFileByPath(@PathVariable("taskType") TaskType taskType,
                                        @RequestParam("filePath") String filePath);


}
