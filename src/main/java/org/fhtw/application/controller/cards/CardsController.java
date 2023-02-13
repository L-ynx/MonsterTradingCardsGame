package org.fhtw.application.controller.cards;

import org.fhtw.application.repository.CardRepository;
import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;

public class CardsController implements Controller {
    public CardsController(CardRepository cardRepo) {
        this.cardRepo = cardRepo;
    }

    private final CardRepository cardRepo;
    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("GET"))
            return showCards();

        return response;
    }

    private Response showCards() {
        return response;
    }
}
