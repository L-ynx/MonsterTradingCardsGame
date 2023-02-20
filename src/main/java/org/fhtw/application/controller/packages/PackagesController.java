package org.fhtw.application.controller.packages;

import org.fhtw.application.model.Card;
import org.fhtw.application.repository.PackageRepository;
import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;
import org.fhtw.http.Status;

import java.util.List;

public class PackagesController implements Controller {

    public PackagesController(PackageRepository packageRepo) {
        this.packageRepo = packageRepo;
    }

    private final PackageRepository packageRepo;
    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("POST"))
            return createPackage(request);

        response.setBody("Wrong method!");
        response.setHttpStatus(Status.BAD_REQUEST);

        return response;
    }

    private Response createPackage(Request request) {
        String token = request.getToken();
        String username = request.getUsername();
        List<Card> cards = request.getBodyAsList(Card.class);

        for (Card card : cards)
            card.getElements();

        if (packageRepo.authenticate(username, token)) {
            if (token.equals("admin-mtcgToken")) {
                if (packageRepo.createPackage(cards)) {
                    response.setHttpStatus(Status.CREATED);
                    response.setBody(serializer.serialize(cards));
                } else {
                    response.setHttpStatus(Status.CONFLICT);
                    response.setBody("At least one card in the packages already exists");
                }
            } else {
                response.setHttpStatus(Status.FORBIDDEN);
                response.setBody("Provided user is not 'admin'");
            }
        } else {
            response.setHttpStatus(Status.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }
        return response;
    }
}
