package ru.lenni.aggregator.resource.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.lenni.aggregator.dto.TaskMetadata;
import ru.lenni.aggregator.resource.DocumentResource;
import ru.lenni.aggregator.resource.TaskResource;
import ru.lenni.aggregator.resource.common.TaskType;
import ru.lenni.aggregator.service.TaskService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DocumentResourceImpl implements DocumentResource {


    @Override
    public ResponseEntity<Resource> processFileWithUpload(TaskType taskType, MultipartFile file) {
        return null;
    }

    @Override
    public ResponseEntity<Resource> processFileByPath(TaskType taskType, String filePath) {
        return null;
    }
}
