package battle;

import org.fhtw.application.controller.game.BattleController;
import org.fhtw.application.repository.GameRepository;
import org.fhtw.http.Request;
import org.fhtw.http.Response;
import org.fhtw.http.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class BattleControllerTest {
    private static BattleController battleController;
    @BeforeAll
    static void initBattleController() {
        GameRepository gameRepo = mock(GameRepository.class);
        battleController = new BattleController(gameRepo);
    }

    // Test if battle works fine
    @Test
    void validBattle() throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Request req1 = new Request();
        Request req2 = new Request();
        req1.setUsername("Midoriya");
        req2.setUsername("Bakugo");

        Method battle = battleController.getClass().getDeclaredMethod("battle", Request.class);
        battle.setAccessible(true);
        Field battleStarted = battleController.getClass().getDeclaredField("battleStarted");
        battleStarted.setAccessible(true);

        Response[] response = new Response[2];

        Thread player1 = new Thread(() -> {
            try {
                response[0] = (Response) battle.invoke(battleController, req1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Thread player2 = new Thread(() -> {
            try {
                response[1] = (Response) battle.invoke(battleController, req2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        player1.start();
        player2.start();

        player2.join();
        player1.join();

        assertEquals("Waiting for opponent...Battle finished", outContent.toString().replaceAll("[\n\r]", ""));
        assertEquals(Status.OK, response[0].getHttpStatus());
        assertEquals(Status.OK, response[1].getHttpStatus());

        System.setOut(System.out);
    }

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
}
