package com.azimo.tukan.log.structure.model;

import java.util.List;

public class HttpResponseDetails {

    private String headers;

    private String body;

    private String code;

    private float time;

    private List<String> context;

    public HttpResponseDetails() {
    }

    public HttpResponseDetails(final String headers, final String body, final String code, final float time, final List<String> context) {
        this.headers = headers;
        this.body = body;
        this.code = code;
        this.time = time;
        this.context = context;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(final String headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public float getTime() {
        return time;
    }

    public void setTime(final float time) {
        this.time = time;
    }

    public List<String> getContext() {
        return context;
    }

    public void setContext(final List<String> context) {
        this.context = context;
    }
}
