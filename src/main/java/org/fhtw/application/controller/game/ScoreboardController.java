package org.fhtw.application.controller.game;

import org.fhtw.application.model.Stats;
import org.fhtw.application.repository.GameRepository;
import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;
import org.fhtw.http.Status;

import java.util.List;

public class ScoreboardController implements Controller {
    public ScoreboardController(GameRepository gameRepo) {
        this.gameRepo = gameRepo;
    }

    private final GameRepository gameRepo;
    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("GET"))
            return retrieveScoreboard(request);

        return response;
    }

    private Response retrieveScoreboard(Request request) {
        String username = request.getUsername();
        String token = request.getToken();

        if (gameRepo.authenticate(username, token)) {
            List<Stats> scoreboard = gameRepo.getScoreboard();

            for (Stats scores : scoreboard)
                System.out.println("STATS: " + scores.getElo() + scores.getUsername());

            response.setHttpStatus(Status.OK);
        } else {
            response.setHttpStatus(Status.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }
        return response;
    }
}
