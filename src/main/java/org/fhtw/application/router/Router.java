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
import org.fhtw.application.repository.*;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Map<String, Controller> handler = new HashMap<>();
    private final Map<String, Repository> repositories;

    public Router(Map<String, Repository> repositories) {
        this.repositories = repositories;

        UserController userController = new UserController((UserRepository) repositories.get("user"));
        LoginController loginController = new LoginController((UserRepository) repositories.get("user"));
        PackagesController packagesController = new PackagesController((PackageRepository) repositories.get("package"));
        TransactionController transactionController = new TransactionController((PackageRepository) repositories.get("package"));
        CardsController cardsController = new CardsController((CardRepository) repositories.get("card"));
        DeckController deckController = new DeckController((CardRepository) repositories.get("card"));
        StatsController statsController = new StatsController((GameRepository) repositories.get("game"));
        ScoreboardController scoreboardController = new ScoreboardController((GameRepository) repositories.get("game"));
        BattleController battleController = new BattleController((GameRepository) repositories.get("game"));

        handler.put("/users", userController);
        handler.put("/sessions", loginController);
        handler.put("/packages", packagesController);
        handler.put("/transactions/packages", transactionController);
        handler.put("/cards", cardsController);
        handler.put("/deck", deckController);
        handler.put("/stats", statsController);
        handler.put("/score", scoreboardController);
        handler.put("/battles", battleController);
        handler.put("/tradings", new TradingController());
    }

    public Controller route(String path) {
        return handler.get(path);
    }
}
