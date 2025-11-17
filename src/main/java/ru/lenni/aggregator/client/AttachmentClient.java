package ru.lenni.aggregator.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "attachment", url = "${app.feign.attachment}", path = "/file")
public interface AttachmentClient {

    @GetMapping("/exists")
    ResponseEntity<Boolean> checkFileExists(@RequestParam("filePath") String filePath);

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> upload(@RequestParam("filePath") String filePath, @RequestPart("file") MultipartFile file);

    @GetMapping("/download")
    ResponseEntity<Resource> download(@RequestParam("filePath") String filePath);
}
