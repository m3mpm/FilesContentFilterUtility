package org.m3mpm;

public class DataWriterException extends Exception {
    public DataWriterException(String message) {
        super(message);
    }

    public DataWriterException(String message, Throwable cause) {
        super(message, cause);
    }
}
