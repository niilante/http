package com.wizzardo.httpserver.request;

import com.wizzardo.httpserver.HttpConnection;
import com.wizzardo.httpserver.MultiValue;

import java.util.List;
import java.util.Map;

/**
 * @author: wizzardo
 * Date: 7/25/14
 */
public class Request {

    private HttpConnection connection;
    private Map<String, MultiValue> headers;
    private Map<String, MultiValue> params;
    private Method method;
    private String path;
    private String queryString;
    private int contentLength = -1;
    private boolean bodyParsed = false;

    SimpleRequestBody body;

    public static enum Method {
        GET, PUT, POST, DELETE, HEAD, TRACE, OPTIONS, CONNECT, PATCH
    }

    public Request(HttpConnection connection, Map<String, MultiValue> headers, Map<String, MultiValue> params, String method, String path, String queryString) {
        this.connection = connection;
        this.headers = headers;
        this.params = params;
        this.method = Method.valueOf(method);
        this.path = path;
        this.queryString = queryString;
    }

    public String path() {
        return path;
    }

    public SimpleRequestBody getBody() {
        return body;
    }

    public int contentLength() {
        if (contentLength == -1)
            contentLength = headerInt(Header.KEY_CONTENT_LENGTH, 0);
        return contentLength;
    }

    public HttpConnection connection() {
        return connection;
    }

    public Map<String, MultiValue> headers() {
        return headers;
    }

    public String header(Header header) {
        return header == null ? null : header(header.value);
    }

    public int headerInt(Header header) {
        return headerInt(header.value);
    }

    public int headerInt(Header header, int def) {
        return headerInt(header.value, def);
    }

    public int headerInt(String header) throws NumberFormatException {
        return Integer.parseInt(header(header));
    }

    public int headerInt(String header, int def) {
        String value = header(header);
        if (value == null)
            return def;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public String header(String key) {
        MultiValue value = headers.get(key);
        return value == null ? null : value.getValue();
    }

    public List<String> headers(String key) {
        MultiValue value = headers.get(key);
        return value == null ? null : value.getValues();
    }

    public Method method() {
        return method;
    }

    public String getQueryString() {
        return queryString;
    }

    public String param(String key) {
        MultiValue value = params().get(key);
        return value == null ? null : value.getValue();
    }

    public List<String> params(String key) {
        MultiValue value = params().get(key);
        return value == null ? null : value.getValues();
    }

    public Map<String, MultiValue> params() {
        if (body != null && !bodyParsed) {
            new RequestReader(headers, params).parseParameters(body.bytes(), 0, contentLength);
            bodyParsed = true;
        }
        return params;
    }

}
