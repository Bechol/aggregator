package ru.lenni.aggregator.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.lenni.aggregator.dto.LlmResponse;
import ru.lenni.aggregator.dto.RequestMetadata;
import ru.lenni.aggregator.resource.common.RequestType;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface RequestService {

    UUID create(RequestType requestType, MultipartFile file) throws IOException;

    RequestMetadata findById(UUID requestId);

    List<RequestMetadata> findAll();

    void deleteById(UUID requestUid);

    void cancelById(UUID requestUid);

    void resend(UUID requestUid);

    void processLlmResult(LlmResponse response);

    Resource getResult(UUID requestId);
}
