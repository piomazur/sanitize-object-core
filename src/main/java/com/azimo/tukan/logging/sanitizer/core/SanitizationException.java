package com.azimo.tukan.logging.sanitizer.core;

public class SanitizationException extends RuntimeException {
    public SanitizationException(String message, Throwable cause) {
        super(message, cause);
    }
    public SanitizationException(Throwable cause) {
        super(cause);
    }
}
