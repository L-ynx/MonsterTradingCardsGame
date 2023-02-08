package org.fhtw.http;

public class Request {
    private String method;
    private String path;
    private String body;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public void print() {
        System.out.println("Method: " + method);
        System.out.println("Path: " + path);
        System.out.println("Body: " + body);
    }
}
