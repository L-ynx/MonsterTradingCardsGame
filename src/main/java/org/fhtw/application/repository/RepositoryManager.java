package org.fhtw.application.repository;

import java.util.HashMap;
import java.util.Map;

public class RepositoryManager {
    private final Map<String, Repository> repositories;

    public RepositoryManager() {
        repositories = new HashMap<>();
        repositories.put("user", new UserRepository());
    }

    public Map<String, Repository> getRepositories() {
        return repositories;
    }
}
