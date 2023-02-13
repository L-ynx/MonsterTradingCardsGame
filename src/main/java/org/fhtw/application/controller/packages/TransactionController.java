package org.fhtw.application.controller.packages;

import org.fhtw.application.repository.CardRepository;
import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;
import org.fhtw.http.Status;

public class TransactionController implements Controller {
    public TransactionController(CardRepository cardRepo) {
        this.cardRepo = cardRepo;
    }

    private final CardRepository cardRepo;
    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("POST"))
            return acquirePackage(request);

        return response;
    }

    private Response acquirePackage(Request request) {
        String username = request.getUsername();
        String token = request.getToken();
        if (cardRepo.authenticate(username, token)) {
            if (cardRepo.enoughMoney(username)) {
                if (cardRepo.buyPackage(username)) {
                    // TODO: IMPLEMENT BUYING LOGIC
                } else {
                    response.setHttpStatus(Status.NOT_FOUND);
                    response.setBody("No card package available for buying");
                }
            } else {
                response.setHttpStatus(Status.FORBIDDEN);
                response.setBody("Not enough money for buying a card package");
            }
        } else {
            response.setHttpStatus(Status.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }
        return response;
    }
}
