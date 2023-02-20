package router;

import org.fhtw.application.controller.cards.CardsController;
import org.fhtw.application.controller.cards.DeckController;
import org.fhtw.application.controller.game.BattleController;
import org.fhtw.application.controller.game.ScoreboardController;
import org.fhtw.application.controller.game.StatsController;
import org.fhtw.application.controller.packages.PackagesController;
import org.fhtw.application.controller.packages.TransactionController;
import org.fhtw.application.controller.users.LoginController;
import org.fhtw.application.controller.users.UserController;
import org.fhtw.application.repository.RepositoryManager;
import org.fhtw.application.router.Router;
import org.fhtw.http.Request;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RouterTest {
    static Router router;

    @BeforeAll
    static void initRouter() {
        RepositoryManager repoManager = new RepositoryManager();
        router = new Router(repoManager.getRepositories());
    }

    // Assert paths getting routed correctly
    @Test
    void properRouting() {
        assertEquals(UserController.class, router.route("/users").getClass());
        assertEquals(LoginController.class, router.route("/sessions").getClass());
        assertEquals(PackagesController.class, router.route("/packages").getClass());
        assertEquals(TransactionController.class, router.route("/transactions/packages").getClass());
        assertEquals(CardsController.class, router.route("/cards").getClass());
        assertEquals(DeckController.class, router.route("/deck").getClass());
        assertEquals(StatsController.class, router.route("/stats").getClass());
        assertEquals(ScoreboardController.class, router.route("/score").getClass());
        assertEquals(BattleController.class, router.route("/battles").getClass());
    }

    @Test
    void wrongPath() {
        assertNull(router.route("/what"));
    }

    // Test if router routes to UserController despite having a username after the path
    @Test
    void pathWithUsername() {
        String req = """
                POST /users/Steven HTTP/1.1\r
                Host: localhost:10001\r
                \r
                """;

        BufferedReader br = new BufferedReader(new StringReader(req));

        Request request = new Request(br);

        assertEquals(UserController.class, router.route(request.getPath()).getClass());
    }
}
