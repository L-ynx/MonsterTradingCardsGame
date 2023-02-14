package org.fhtw.application.controller.cards;

import org.fhtw.application.model.Card;
import org.fhtw.application.repository.CardRepository;
import org.fhtw.application.repository.PackageRepository;
import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;
import org.fhtw.http.Status;

import java.util.List;

public class CardsController implements Controller {

    private final CardRepository cardRepo;
    public CardsController(CardRepository cardRepo) {
        this.cardRepo = cardRepo;
    }

    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("GET"))
            return showCards(request);

        return response;
    }

    private Response showCards(Request request) {
        String username = request.getUsername();
        String token = request.getToken();
        List<Card> cards;

        if (cardRepo.authenticate(username, token)) {
            cards = cardRepo.showCards(username, "cards");
            if (!cards.isEmpty()) {
                response.setHttpStatus(Status.OK);
                response.setBody("Cards: ");
                // TODO: RETURN CARDS IN JSON BODY
                for (Card card : cards)
                    System.out.println("ID: " + card.getId() + "\nName: " + card.getCardName() + "\nDamage: " + card.getDamage());
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
