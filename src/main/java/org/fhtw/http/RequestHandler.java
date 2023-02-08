package org.fhtw.http;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RequestHandler implements Runnable {
    private final Socket socket;

    private BufferedReader br;
    private OutputStream out;

    public RequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Request request = new Request(br);

            out = socket.getOutputStream();
            String response = "HTTP/1.0 200 OK\r\n\r\nHello, World!";
            out.write(response.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeClient();
        }
    }


    private void closeClient() {
        try {
            if (br != null)
                br.close();
            if (out != null)
                out.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
