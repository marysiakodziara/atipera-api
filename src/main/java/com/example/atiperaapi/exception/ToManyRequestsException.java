package com.example.atiperaapi.exception;

public class ToManyRequestsException extends RuntimeException{
    public ToManyRequestsException(String message) {super(message);}
}
