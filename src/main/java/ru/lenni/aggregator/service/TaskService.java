package ru.lenni.aggregator.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.lenni.aggregator.dto.LlmResponse;
import ru.lenni.aggregator.dto.TaskMetadata;
import ru.lenni.aggregator.resource.common.TaskType;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    TaskMetadata createTask(TaskType taskType, String filePath);

    TaskMetadata createTask(TaskType taskType, String filePath, MultipartFile file);

    TaskMetadata findById(UUID taskUid);

    List<TaskMetadata> findAll();

    void deleteById(UUID taskUid);

    void cancelById(UUID taskUid);

    void resend(UUID taskUid);

    void processLlmResult(LlmResponse response);

    ResponseEntity<Resource> downloadResult(UUID taskUid);
}
