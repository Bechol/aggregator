package ru.lenni.aggregator.exception;

public class FileNotExistsException extends RuntimeException {

    public FileNotExistsException(String filePath) {
        super(String.format("File not found by path [%s]", filePath));
    }
}
