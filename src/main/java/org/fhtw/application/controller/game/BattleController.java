package org.fhtw.application.controller.game;

import org.fhtw.application.repository.GameRepository;
import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;
import org.fhtw.http.Status;

import java.util.ArrayList;
import java.util.List;



public class BattleController implements Controller {
    public BattleController(GameRepository gameRepo) {
        this.gameRepo = gameRepo;
    }

    private final List<String> lobby = new ArrayList<>();
    private final int TIMEOUT = 10;     // Seconds
    private boolean battleStarted = false;
    private final GameRepository gameRepo;

    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("POST")) {
            if (gameRepo.authenticate(request.getUsername(), request.getToken()))
                return battle(request);
            else {
                response.setHttpStatus(Status.UNAUTHORIZED);
                response.setBody("Access token is missing or invalid");
            }
        }
        response.setBody("Wrong method!");
        response.setHttpStatus(Status.BAD_REQUEST);

        return response;
    }

    private synchronized Response battle(Request request) {
        String username = request.getUsername();
        lobby.add(username);
        String user1 = lobby.get(0);

        if (lobby.size() == 1) {
            System.out.println("Waiting for opponent...\n");

            try {
                wait(TIMEOUT * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!battleStarted) {
                lobby.remove(user1);

                response.setHttpStatus(Status.OK);
                response.setBody("No opponents available");

                System.out.println("No opponents available");
            } else {
                battleStarted = false;
            }
            return response;
        }

        if (lobby.size() == 2) {
            battleStarted = true;
            String user2 = lobby.get(1);

            String battleLog = gameRepo.startBattle(user1, user2);
            System.out.println("Battle finished");

            notify();

            lobby.remove(user1);
            lobby.remove(user2);

            response.setHttpStatus(Status.OK);
            response.setBody(battleLog);
        }
        return response;
    }
}
