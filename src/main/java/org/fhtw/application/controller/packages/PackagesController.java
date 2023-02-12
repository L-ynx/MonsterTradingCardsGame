package org.fhtw.application.controller.packages;

import org.fhtw.application.model.Card;
import org.fhtw.application.model.Packages;
import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;

import java.util.List;

public class PackagesController implements Controller {

    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("POST"))
            return createPackage(request);

        return response;
    }

    private Response createPackage(Request request) {
        List<Card> cards = request.getBodyAsList(Card.class);
        for (Card card : cards) {
            System.out.println("Id: " + card.getId() + ", Name: " + card.getCardName() + ", Damage: " + card.getDamage());
        }
        Packages pkg = new Packages(cards);
        return response;
    }
}
