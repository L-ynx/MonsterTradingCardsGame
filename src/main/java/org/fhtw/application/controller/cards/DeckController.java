package org.fhtw.application.controller.cards;

import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;

public class DeckController implements Controller {
    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("GET"))
            return showDeck();
        else if (request.getMethod().equals("PUT"))
            return configureDeck();

        return response;
    }

    private Response configureDeck() {
        return response;
    }

    private Response showDeck() {
        return response;
    }
}
