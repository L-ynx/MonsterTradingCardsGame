package battle;

import org.fhtw.application.controller.game.BattleController;
import org.fhtw.application.repository.CardRepository;
import org.fhtw.application.repository.GameRepository;
import org.fhtw.http.Request;
import org.fhtw.http.Response;
import org.fhtw.http.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class BattleControllerTest {
    private static BattleController battleController;
    @BeforeAll
    static void initBattleController() {
        CardRepository cardRepo = new CardRepository();
        GameRepository gameRepo = new GameRepository(cardRepo);
        battleController = new BattleController(gameRepo);
    }

    // Test if battle works fine

    // Only 1 User in Lobby
    @Test
    void testTimeOut() throws Exception {
        Request request = new Request();
        request.setUsername("Midoriya");

        Method battle = battleController.getClass().getDeclaredMethod("battle", Request.class);
        battle.setAccessible(true);
        Response response = (Response) battle.invoke(battleController, request);

        assertEquals(Status.OK, response.getHttpStatus());
        assertEquals("No opponents available", response.getBody());
    }

    @Test
    void wrongMethod() {
        Request request = new Request();
        request.setMethod("PUT");

        Response response = battleController.process(request);

        assertEquals("Wrong method!", response.getBody());
        assertEquals(Status.BAD_REQUEST, response.getHttpStatus());
    }

    //@Test
    void wrongCredentials() {
        // Database Connection needed
        Request request = new Request();
        request.setMethod("POST");
        request.setUsername("kienboec");
        request.setToken("nonExistent-mtcgToken");

        Response response = battleController.process(request);

        assertEquals("Access token is missing or invalid", response.getBody());
        assertEquals(Status.UNAUTHORIZED, response.getHttpStatus());
    }
}
