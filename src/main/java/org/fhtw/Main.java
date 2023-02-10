package org.fhtw;

import org.fhtw.application.router.Router;
import org.fhtw.http.Server;

public class Main {
    public static void main(String[] args) {
        Router router = new Router();
        Server server = new Server(router);

        server.start();
    }
}