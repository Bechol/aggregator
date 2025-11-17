package ru.lenni.aggregator.resource.impl;

import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.lenni.aggregator.dto.TaskMetadata;
import ru.lenni.aggregator.resource.TaskResource;
import ru.lenni.aggregator.resource.common.TaskType;
import ru.lenni.aggregator.service.TaskService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TaskResourceImpl implements TaskResource {

    private final TaskService taskService;

    @Override
    public ResponseEntity<TaskMetadata> createTask(TaskType taskType, String filePath) {
        return ResponseEntity.ok(taskService.createTask(taskType, filePath));
    }

    @Override
    public ResponseEntity<TaskMetadata> createTask(TaskType taskType, String filePath, MultipartFile file) {
        return ResponseEntity.ok(taskService.createTask(taskType, filePath, file));
    }

    @Override
    public ResponseEntity<TaskMetadata> findTaskById(UUID taskUid) {
        return ResponseEntity.ok(taskService.findById(taskUid));
    }

    @Override
    public ResponseEntity<List<TaskMetadata>> findAll() {
        return ResponseEntity.ok(taskService.findAll());
    }

    @Override
    public void deleteByUid(UUID taskUid) {
        taskService.deleteById(taskUid);
    }

    @Override
    public void cancelById(UUID taskUid) {
        taskService.cancelById(taskUid);
    }

    @Override
    public void resend(UUID taskUid) {
        taskService.resend(taskUid);
    }

    @Override
    public ResponseEntity<Resource> downloadResult(UUID taskUid) {
        return taskService.downloadResult(taskUid);
    }
}
