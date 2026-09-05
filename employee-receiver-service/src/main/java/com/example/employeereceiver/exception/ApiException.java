package com.example.employeereceiver.exception;

public class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
    }
}
