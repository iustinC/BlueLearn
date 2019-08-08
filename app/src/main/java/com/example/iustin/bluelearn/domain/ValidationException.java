package com.example.iustin.bluelearn.domain;

public class ValidationException extends Exception {
    public ValidationException(final String exceptionMesssage) {
        super(exceptionMesssage);
    }
}
