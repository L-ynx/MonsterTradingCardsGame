package org.fhtw.http;

public class Response {

    private String contentType;
    private Status httpStatus = Status.BAD_REQUEST;
    private String body;

    protected String responseBuilder() {
        return "HTTP1/1 " + httpStatus.getStatusCode() + " " + httpStatus.getStatusMessage() + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "\r\n" +
                body;
    }
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public Status getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Status httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
