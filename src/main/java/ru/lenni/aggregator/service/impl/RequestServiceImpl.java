package ru.lenni.aggregator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.lenni.aggregator.client.AttachmentClient;
import ru.lenni.aggregator.dto.LlmResponseDto;
import ru.lenni.aggregator.dto.RequestMetadata;
import ru.lenni.aggregator.dto.RequestStatus;
import ru.lenni.aggregator.dto.RequestType;
import ru.lenni.aggregator.exception.*;
import ru.lenni.aggregator.model.Request;
import ru.lenni.aggregator.repository.RequestRepository;
import ru.lenni.aggregator.service.LlmSenderService;
import ru.lenni.aggregator.service.RequestService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private static final List<String> ALLOWED_EXTENSIONS = List.of("pdf", "png", "jpg", "jpeg", "txt");

    private final RequestRepository requestRepository;
    private final LlmSenderService llmSenderService;
    private final AttachmentClient attachmentClient;

    @Override
    @Transactional
    public UUID create(RequestType requestType, MultipartFile file) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalExtensionException(extension);
        }
        Request request = Request.withType(requestType);
        String s3Key = attachmentClient.upload(file).toString();
        request.setS3Key(s3Key);
        requestRepository.save(request);
        llmSenderService.sendRequestToLlm(request);
        return request.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public RequestMetadata findById(UUID requestId) {
        return requestRepository.findById(requestId)
                .filter(Request::isNotDeleted)
                .map(this::mapMetadataResponse)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
    }

    @Override
    @Transactional
    public List<RequestMetadata> findAll() {
        return requestRepository.findByDeletedFalseOrderByLastUpdateTimeDesc()
                .stream().map(this::mapMetadataResponse).toList();
    }

    @Override
    @Transactional
    public void deleteById(UUID taskUid) {
        requestRepository.softDeleteById(taskUid);
    }

    @Override
    @Transactional
    public void cancelById(UUID taskUid) {
        Request request = requestRepository.findById(taskUid)
                .filter(Request::isNotDeleted).orElseThrow(() -> new RequestNotFoundException(taskUid));
        request.setDeleted(true);
        request.setStatus(RequestStatus.CANCELLED);
        llmSenderService.sendRequestToLlm(requestRepository.saveAndFlush(request));
    }

    @Override
    @Transactional
    public void resend(UUID taskUid) {
        Request errorSendRequest = requestRepository.findById(taskUid)
                .filter(task -> task.getStatus().equals(RequestStatus.SEND_ERROR))
                .orElseThrow(() -> new RequestNotFoundException(taskUid));
        llmSenderService.resendErrorTask(errorSendRequest);
    }

    @Override
    @Transactional
    public void processLlmResult(LlmResponseDto response) {
        UUID requestId = response.getRequestId();
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new RequestNotFoundException(requestId));
        request.setStatus(RequestStatus.DONE);
        request.setContainsPD(response.getContainsPD());
        request.setResultS3Key(response.getResultS3Key());
        requestRepository.save(request);
        //todo оповещение ИБ (строго после сохранения) что это будет? отдельная таблица с веб мордой? письмо?
    }


    @Override
    @Transactional(readOnly = true)
    public Resource getResult(UUID requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new RequestNotFoundException(requestId));
        if (RequestStatus.DONE.equals(request.getStatus())) {
            return attachmentClient.download(request.getResultS3Key());
        } else {
            throw new RequestNotProcessedYetException(requestId);
        }
    }

    private RequestMetadata mapMetadataResponse(Request request) {
        return new RequestMetadata(
                request.getId(),
                request.getType().name(),
                request.getCreationTime(),
                request.getLastUpdateTime(),
                request.getStatus().name()
        );
    }
}
