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

    @ExceptionHandler(exception = {RequestNotProcessedYetException.class, IllegalExtensionException.class})
    public ResponseEntity<String> handleExceptionWithBadRequest(RequestNotProcessedYetException exception) {
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

    @ExceptionHandler(exception = RequestNotFoundException.class)
    public ResponseEntity<String> handleFileNotExistsException(RequestNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }
}
