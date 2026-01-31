package ru.lenni.aggregator.resource.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.lenni.aggregator.dto.RequestMetadata;
import ru.lenni.aggregator.dto.RequestType;
import ru.lenni.aggregator.resource.RequestResource;
import ru.lenni.aggregator.service.RequestService;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RequestResourceImpl implements RequestResource {

    private final RequestService requestService;

    @Override
    public UUID create(RequestType type, MultipartFile file) throws IOException {
        return requestService.create(type, file);
    }

    @Override
    public RequestMetadata findById(UUID requestId) {
        return requestService.findById(requestId);
    }

    @Override
    public Resource getResult(UUID requestId) {
        return requestService.getResult(requestId);
    }
}
