package org.fhtw.http;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RequestHandler implements Runnable {
    private final Socket socket;

    public RequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Request request = parseRequest();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeClient();
        }
    }


    private Request parseRequest() throws IOException {
        Request request = new Request();
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String versionString = br.readLine();
        final String[] splitVersionString = versionString.split(" ");
        request.setMethod(splitVersionString[0]);
        request.setPath(splitVersionString[1]);

        String line;
        int contentLength = 0;
        while ((line = br.readLine()).length() > 0) {
            if (line.startsWith("Content-Length: ")) {
                contentLength = Integer.parseInt(line.substring("Content-Length: ".length()));
            }
        }

        char[] buffer = new char[contentLength];
        br.read(buffer, 0, contentLength);
        request.setBody(new String(buffer));
        request.print();

        br.close();

        return request;
    }

    private void closeClient() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
