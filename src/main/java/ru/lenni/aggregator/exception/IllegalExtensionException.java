package ru.lenni.aggregator.exception;

public class IllegalExtensionException extends RuntimeException {

    public IllegalExtensionException(String extension) {
        super(String.format("Extension %s is not allowed", extension));
    }
}
