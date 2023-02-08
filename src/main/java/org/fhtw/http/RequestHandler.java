package org.fhtw.http;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private final Socket socket;

    public RequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        Request request = parseRequest();
    }

    private Request parseRequest() throws IOException {
        Request request = new Request();
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void handleRequest() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        OutputStream os = socket.getOutputStream();


        StringBuilder request = new StringBuilder();
        while (br.ready()) {
            request.append((char) br.read());
        }

        System.out.println("Received request:\n" + request);
        String response = "HTTP/1.0 200 OK\\r\\n\\r\\nHello, World!";
        os.write(response.getBytes());
        br.close();
        os.close();
        socket.close();
    }
}
