package org.fhtw.application.controller.cards;

import org.fhtw.application.model.Card;
import org.fhtw.application.repository.CardRepository;
import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;
import org.fhtw.http.Status;

import java.util.List;

public class DeckController implements Controller {
    public DeckController(CardRepository cardRepo) {
        this.cardRepo = cardRepo;
    }

    private final CardRepository cardRepo;
    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("GET"))
            return showDeck(request);
        else if (request.getMethod().equals("PUT"))
            return configureDeck(request);

        return response;
    }

    private Response configureDeck(Request request) {
        String username = request.getUsername();
        String token = request.getToken();
        List<String> deck = request.getBodyAs(List.class);

        if (cardRepo.authenticate(username, token)) {
            if (deck.size() == 4) {
                if (cardRepo.configureDeck(username, deck)) {
                    response.setHttpStatus(Status.OK);
                    response.setBody(serializer.serialize(deck));
                } else {
                    response.setHttpStatus(Status.FORBIDDEN);
                    response.setBody("At least one of the provided cards does not belong to the user or is not available.");
                }
            } else {
                //response.setHttpStatus(Status.BAD_REQUEST);
                response.setBody("The provided deck did not include the required amount of cards");
            }
        } else {
            response.setHttpStatus(Status.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }

        return response;
    }

    private Response showDeck(Request request) {
        String username = request.getUsername();
        String token = request.getToken();
        if (cardRepo.authenticate(username, token)) {
            List<Card> cards = cardRepo.showCards(username, "decks");
            if (!cards.isEmpty()) {
                response.setHttpStatus(Status.OK);
                response.setContentType("application/json");
                response.setBody(serializer.serialize(cards));
            } else {
                response.setHttpStatus(Status.NO_CONTENT);
            }
        } else {
            response.setHttpStatus(Status.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }
        return response;
    }
}
