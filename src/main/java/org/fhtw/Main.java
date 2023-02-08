package org.fhtw;

import org.fhtw.http.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}