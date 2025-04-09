package com.example.exception;

public class ApplicationDeadlineException extends Exception {
    public ApplicationDeadlineException(String message) {
        super(message);
    }

    public ApplicationDeadlineException(String message, Throwable cause) {
        super(message, cause);
    }
}
