package org.fhtw;

import org.fhtw.application.repository.RepositoryManager;
import org.fhtw.application.router.Router;
import org.fhtw.http.Server;

public class Main {
    public static void main(String[] args) {
        RepositoryManager repoManager = new RepositoryManager();
        Router router = new Router(repoManager.getRepositories());
        Server server = new Server(router);

        server.start();
    }
}