package ru.lenni.aggregator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.lenni.aggregator.client.AttachmentClient;
import ru.lenni.aggregator.dto.LlmResponse;
import ru.lenni.aggregator.dto.TaskMetadata;
import ru.lenni.aggregator.exception.*;
import ru.lenni.aggregator.model.Task;
import ru.lenni.aggregator.model.TaskStatus;
import ru.lenni.aggregator.repository.TaskRepository;
import ru.lenni.aggregator.resource.common.TaskType;
import ru.lenni.aggregator.service.LlmSenderService;
import ru.lenni.aggregator.service.TaskService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final LlmSenderService llmSenderService;
    private final AttachmentClient attachmentClient;

    @Override
    @Transactional
    public TaskMetadata createTask(TaskType taskType, String filePath) {
        //todo проверки на валидность заданного пути
        if (isFileExists(filePath)) {
            Task task = Task.withType(taskType).setFilePath(filePath).setStatus(TaskStatus.NEW);
            llmSenderService.sendRequestToLlm(taskRepository.saveAndFlush(task)); //todo сделать callback, который удалит созданную таску, либо сделать шедулер, который будет отправлять с заданным интервалом
            return mapMetadataResponse(task);
        } else {
            throw new FileNotExistsException(filePath);
        }
    }

    @Override
    @Transactional
    public TaskMetadata createTask(TaskType taskType, String filePath, MultipartFile file) {
        if (isFileExists(filePath)) {
            throw new FileAlreadyExistsException(filePath);
        } else {
            boolean uploadResult = attachmentClient.upload(filePath, file).getStatusCode().is2xxSuccessful();
            if (uploadResult) {
                Task task = Task.withType(taskType).setFilePath(filePath).setStatus(TaskStatus.NEW);
                llmSenderService.sendRequestToLlm(taskRepository.saveAndFlush(task));
                return mapMetadataResponse(task);
            } else {
                throw new AttachmentUploadException(filePath);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TaskMetadata findById(UUID taskUid) {
        return taskRepository.findById(taskUid)
                .filter(Task::isNotDeleted)
                .map(this::mapMetadataResponse)
                .orElseThrow(() -> new TaskNotFoundException(taskUid));
    }

    @Override
    @Transactional
    public List<TaskMetadata> findAll() {
        return taskRepository.findByDeletedFalseOrderByLastUpdateTimeDesc()
                .stream().map(this::mapMetadataResponse).toList();
    }

    @Override
    @Transactional
    public void deleteById(UUID taskUid) {
        taskRepository.softDeleteById(taskUid);
    }

    @Override
    @Transactional
    public void cancelById(UUID taskUid) {
        Task task = taskRepository.findById(taskUid)
                .filter(Task::isNotDeleted).orElseThrow(() -> new TaskNotFoundException(taskUid));
        task.setDeleted(true);
        task.setStatus(TaskStatus.CANCELLED);
        llmSenderService.sendRequestToLlm(taskRepository.saveAndFlush(task));
    }

    @Override
    @Transactional
    public void resend(UUID taskUid) {
        Task errorSendTask = taskRepository.findById(taskUid)
                .filter(task -> task.getStatus().equals(TaskStatus.SEND_ERROR))
                .orElseThrow(() -> new TaskNotFoundException(taskUid));
        llmSenderService.resendErrorTask(errorSendTask);
    }

    @Override
    @Transactional
    public void processLlmResult(LlmResponse response) {
        UUID taskUid = response.getTaskUid();
        Task task = taskRepository.findById(taskUid).orElseThrow(() -> new TaskNotFoundException(taskUid));
        task.setStatus(TaskStatus.DONE);
        task.setContainsPD(response.getContainsPD());
        task.setResultFilePath(response.getResultPath());
        taskRepository.save(task);
        //todo оповещение ИБ (строго после сохранения) что это будет? отдельная таблица с веб мордой? письмо?
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Resource> downloadResult(UUID taskUid) {
        Task task = taskRepository.findById(taskUid).orElseThrow(() -> new TaskNotFoundException(taskUid));
        if (TaskStatus.DONE.equals(task.getStatus())) {
            return attachmentClient.download(task.getResultFilePath());
        } else {
            throw new TaskNotProcessedYetException(taskUid);
        }
    }

    private boolean isFileExists(String filePath) {
        return attachmentClient.checkFileExists(filePath).getStatusCode().is2xxSuccessful();
    }

    private TaskMetadata mapMetadataResponse(Task task) {
        return new TaskMetadata(
                task.getId(),
                task.getTaskType().name(),
                task.getFilePath(),
                task.getCreationTime(),
                task.getLastUpdateTime(),
                task.getStatus().name()
        );
    }
}
