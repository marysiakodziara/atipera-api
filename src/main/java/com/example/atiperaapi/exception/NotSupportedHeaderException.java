package com.example.atiperaapi.exception;

public class NotSupportedHeaderException extends RuntimeException {
    public NotSupportedHeaderException(String message) {
        super(message);
    }
}