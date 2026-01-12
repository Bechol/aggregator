package ru.lenni.aggregator.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.lenni.aggregator.config.FeignMultipartConfig;

import java.util.UUID;

@FeignClient(name = "attachment", url = "${app.feign.attachment}", path = "/file", configuration = FeignMultipartConfig.class)
public interface AttachmentClient {

    @PostMapping(path = "/upload/lite", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    UUID upload(@RequestPart("file") MultipartFile file);

    @GetMapping("/download/result")
    ResponseEntity<Resource> download(@RequestParam("resultS3Key") String resultS3Key);
}
