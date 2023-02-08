package org.fhtw.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int PORT = 10001;

    public void start() {
        ThreadGroup group = new ThreadGroup("MyThreadGroup");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Listening on port: " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection: " + socket.getInetAddress());
                RequestHandler request = new RequestHandler(socket);
                new Thread(group, request).start();

                // List current threads for testing purposes
                Thread[] threads = new Thread[group.activeCount()];
                group.enumerate(threads);
                for (Thread t : threads) {
                    System.out.println("Thread: " + t.getName() + ", State: " + t.getState());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
