package ru.lenni.aggregator.resource;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.lenni.aggregator.dto.RequestMetadata;
import ru.lenni.aggregator.dto.RequestType;

import java.io.IOException;
import java.util.UUID;

@RequestMapping("/request")
public interface RequestResource {

    @PostMapping("/{type}")
    @ResponseStatus(HttpStatus.CREATED)
    UUID create(@PathVariable("type") RequestType type,
                @RequestParam("file") MultipartFile file) throws IOException;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    RequestMetadata findById(@PathVariable("id") UUID requestId);

    @GetMapping("/result/{id}")
    Resource getResult(@PathVariable("id") UUID requestId);

}
