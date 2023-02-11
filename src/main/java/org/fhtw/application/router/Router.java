package org.fhtw.application.router;

import org.fhtw.application.controller.cards.CardsController;
import org.fhtw.application.controller.cards.DeckController;
import org.fhtw.application.controller.game.BattleController;
import org.fhtw.application.controller.game.ScoreboardController;
import org.fhtw.application.controller.game.StatsController;
import org.fhtw.application.controller.packages.TransactionController;
import org.fhtw.application.controller.trading.TradingController;
import org.fhtw.application.controller.users.LoginController;
import org.fhtw.application.controller.packages.PackagesController;
import org.fhtw.application.controller.users.UserController;
import org.fhtw.application.repository.Repository;
import org.fhtw.application.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Map<String, Controller> handler = new HashMap<>();
    private final Map<String, Repository> repositories;

    public Router(Map<String, Repository> repositories) {
        this.repositories = repositories;

        handler.put("/users", new UserController((UserRepository) repositories.get("user")));
        handler.put("/sessions", new LoginController());
        handler.put("/packages", new PackagesController());
        handler.put("/transactions/packages", new TransactionController());
        handler.put("/cards", new CardsController());
        handler.put("/deck", new DeckController());
        handler.put("/stats", new StatsController());
        handler.put("/scoreboard", new ScoreboardController());
        handler.put("/battles", new BattleController());
        handler.put("/tradings", new TradingController());
    }

    public Controller route(String path) {
        return handler.get(path);
    }
}