package com.littlepay.exceptions;

public class RuntimeValidationException extends RuntimeException {
    public RuntimeValidationException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
