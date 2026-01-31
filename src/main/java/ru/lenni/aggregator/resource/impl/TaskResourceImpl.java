package ru.lenni.aggregator.resource.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.lenni.aggregator.dto.RequestMetadata;
import ru.lenni.aggregator.resource.TaskResource;
import ru.lenni.aggregator.service.RequestService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TaskResourceImpl implements TaskResource {

    private final RequestService requestService;

    @Override
    public ResponseEntity<List<RequestMetadata>> findAll() {
        return ResponseEntity.ok(requestService.findAll());
    }

    @Override
    public void deleteByUid(UUID taskUid) {
        requestService.deleteById(taskUid);
    }

    @Override
    public void cancelById(UUID taskUid) {
        requestService.cancelById(taskUid);
    }

    @Override
    public void resend(UUID taskUid) {
        requestService.resend(taskUid);
    }

}
