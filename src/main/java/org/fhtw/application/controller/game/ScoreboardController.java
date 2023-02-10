package org.fhtw.application.controller.game;

import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;

public class ScoreboardController implements Controller {
    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("GET"))
            return retrieveScoreboard();

        return response;
    }

    private Response retrieveScoreboard() {
        return response;
    }
}
