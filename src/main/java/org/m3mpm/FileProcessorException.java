package org.m3mpm;

public class FileProcessorException extends Exception {
    public FileProcessorException(String message) {
        super(message);
    }

    public FileProcessorException(String message, Throwable cause) {
        super(message, cause);
    }
}
