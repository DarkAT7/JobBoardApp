package com.example.exception;

public class InvalidSalaryException extends Exception {
    public InvalidSalaryException(String message) {
        super(message);
    }

    public InvalidSalaryException(String message, Throwable cause) {
        super(message, cause);
    }
}
