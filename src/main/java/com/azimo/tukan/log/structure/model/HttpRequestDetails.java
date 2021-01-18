package com.azimo.tukan.log.structure.model;

import java.util.List;

public class HttpRequestDetails {

    private String path;

    private String headers;

    private String body;

    private List<String> param;

    private List<String> context;

    public HttpRequestDetails() {
    }

    public HttpRequestDetails(final String path, final String headers, final String body, final List<String> param, final List<String> context) {
        this.path = path;
        this.headers = headers;
        this.body = body;
        this.param = param;
        this.context = context;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
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

    public List<String> getParam() {
        return param;
    }

    public void setParam(final List<String> param) {
        this.param = param;
    }

    public List<String> getContext() {
        return context;
    }

    public void setContext(final List<String> context) {
        this.context = context;
    }
}
