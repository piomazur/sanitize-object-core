package com.azimo.tukan.log.structure.model;

import java.util.List;

public class ExceptionDetails {
    private String message;

    private String stackTrace;

    private List<String> context;

    public ExceptionDetails() {
    }

    public ExceptionDetails(final String message, final String stackTrace, final List<String> context) {
        this.message = message;
        this.stackTrace = stackTrace;
        this.context = context;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(final String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public List<String> getContext() {
        return context;
    }

    public void setContext(final List<String> context) {
        this.context = context;
    }
}
