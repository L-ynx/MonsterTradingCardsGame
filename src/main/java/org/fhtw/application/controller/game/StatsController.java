package org.fhtw.application.controller.game;

import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;

public class StatsController implements Controller {
    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("GET"))
            return retrieveStats();

        return response;
    }

    private Response retrieveStats() {
        return response;
    }
}
