package ru.lenni.aggregator.exception;

public class AttachmentUploadException extends RuntimeException {

    public AttachmentUploadException(String filePath) {
        super(String.format("Failed upload to s3 for [%s]", filePath));
    }
}
