package org.fhtw.http;

import java.io.BufferedReader;
import java.io.IOException;

public class Request {
    private String method;
    private String path;
    private String body;

    public Request(BufferedReader br) {
        parseRequest(br);
    }

    private void parseRequest(BufferedReader br) {
        try {
            String versionString = br.readLine();
            final String[] splitVersionString = versionString.split(" ");
            method = splitVersionString[0];
            path = splitVersionString[1];

            String line;
            int contentLength = 0;
            while (!(line = br.readLine()).equals("")) {
                if (line.startsWith("Content-Length: ")) {
                    contentLength = Integer.parseInt(line.substring("Content-Length: ".length()));
                }
            }

            char[] buffer = new char[contentLength];
            br.read(buffer, 0, contentLength);
            body = new String(buffer);
            print();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
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
