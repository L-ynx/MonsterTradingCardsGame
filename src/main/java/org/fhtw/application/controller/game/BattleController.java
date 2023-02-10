package org.fhtw.application.controller.game;

import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;

public class BattleController implements Controller {
    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("POST"))
            return battle();

        return response;
    }

    private Response battle() {
        return response;
    }
}
