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

        response.setBody("Wrong method!");
        response.setHttpStatus(Status.BAD_REQUEST);

        return response;
    }

    private Response retrieveScoreboard(Request request) {
        String username = request.getUsername();
        String token = request.getToken();

        if (gameRepo.authenticate(username, token)) {
            List<Stats> scoreboard = gameRepo.getScoreboard();

            response.setHttpStatus(Status.OK);
            response.setContentType("application/json");
            response.setBody(serializer.serialize(scoreboard));
        } else {
            response.setHttpStatus(Status.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }
        return response;
    }
}
