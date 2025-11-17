package ru.lenni.aggregator.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(exception = FileAlreadyExistsException.class)
    public ResponseEntity<String> handleFileAlreadyExistsException(FileAlreadyExistsException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(exception = TaskNotProcessedYetException.class)
    public ResponseEntity<String> handleFileAlreadyExistsException(TaskNotProcessedYetException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(exception = AttachmentUploadException.class)
    public ResponseEntity<String> handleAttachmentUploadException(AttachmentUploadException exception) {
        return ResponseEntity.status(500).body(exception.getMessage());
    }

    @ExceptionHandler(exception = FileNotExistsException.class)
    public ResponseEntity<String> handleFileNotExistsException(FileNotExistsException exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(exception = TaskNotFoundException.class)
    public ResponseEntity<String> handleFileNotExistsException(TaskNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }
}
