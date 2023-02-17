package org.fhtw.application.repository;

import java.util.HashMap;
import java.util.Map;

public class RepositoryManager {
    private final Map<String, Repository> repositories;

    public RepositoryManager() {
        repositories = new HashMap<>();

        UserRepository userRepo = new UserRepository();
        PackageRepository packageRepo = new PackageRepository();
        CardRepository cardRepo = new CardRepository();
        GameRepository gameRepo = new GameRepository(cardRepo);

        repositories.put("user", userRepo);
        repositories.put("package", packageRepo);
        repositories.put("card", cardRepo);
        repositories.put("game", gameRepo);
    }

    public Map<String, Repository> getRepositories() {
        return repositories;
    }
}
