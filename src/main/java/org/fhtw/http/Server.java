package org.fhtw.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int PORT = 10001;

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Listening on port: " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection: " + socket.getInetAddress());
                RequestHandler request = new RequestHandler(socket);
                new Thread(request).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
