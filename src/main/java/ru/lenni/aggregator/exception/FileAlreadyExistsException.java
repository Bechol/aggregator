package ru.lenni.aggregator.exception;

public class FileAlreadyExistsException extends RuntimeException {

    public FileAlreadyExistsException(String filePath) {
        super(String.format("File with path [%s] is already exists", filePath));
    }
}
