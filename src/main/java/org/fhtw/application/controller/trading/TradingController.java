package org.fhtw.application.controller.trading;

import org.fhtw.application.model.Trade;
import org.fhtw.application.repository.TradingRepository;
import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;
import org.fhtw.http.Status;

import java.util.List;

public class TradingController implements Controller {
    public TradingController(TradingRepository tradingRepo) {
        this.tradingRepo = tradingRepo;
    }

    private final TradingRepository tradingRepo;
    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("GET"))
            return retrieveDeals(request);
        if (request.getMethod().equals("POST")) {
            if (request.getTradingId() != null)
                return trade(request);
            return createDeal(request);
        }
        if (request.getMethod().equals("DELETE"))
            return deleteDeal(request);

        response.setBody("Wrong method!");
        response.setHttpStatus(Status.BAD_REQUEST);

        return response;
    }

    private Response trade(Request request) {
        String username = request.getUsername();
        String token = request.getToken();

        if(tradingRepo.authenticate(username, token)) {
            String dealId = request.getTradingId();
            String tradingCardId = request.getBody().replaceAll("\"", "");

            if (tradingRepo.dealExists(dealId)) {
                if(tradingRepo.trade(dealId, username, tradingCardId)) {
                    response.setHttpStatus(Status.OK);
                    response.setBody("Trading deal successfully executed");
                } else {
                    response.setHttpStatus(Status.FORBIDDEN);
                    response.setBody("The offered card is not owned by the user, the requirements are not met " +
                                     "(Type, MinimumDamage), the offered card is locked in the deck or the user " +
                                     "tried to trade with himself");
                }
            } else {
                response.setHttpStatus(Status.NOT_FOUND);
                response.setBody("The provided deal ID was not found");
            }
        } else {
            response.setHttpStatus(Status.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }
        return response;
    }

    private Response createDeal(Request request) {
        String username = request.getUsername();
        String token = request.getToken();

        if(tradingRepo.authenticate(username, token)) {
            Trade trade = request.getBodyAs(Trade.class);

            if (!tradingRepo.dealExists(trade.getId())) {
                if(tradingRepo.createDeal(trade)) {
                    response.setHttpStatus(Status.CREATED);
                    response.setBody(serializer.serialize(trade));
                } else {
                    response.setHttpStatus(Status.FORBIDDEN);
                    response.setBody("The provided card does not belong to the user or is not available.");
                }
            } else {
                response.setHttpStatus(Status.CONFLICT);
                response.setBody("A deal with this deal ID already exists.");
            }
        } else {
            response.setHttpStatus(Status.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }
        return response;
    }

    private Response retrieveDeals(Request request) {
        String username = request.getUsername();
        String token = request.getToken();

        if(tradingRepo.authenticate(username, token)) {
            List<Trade> deals = tradingRepo.getDeals();

            if (!deals.isEmpty()) {
                response.setHttpStatus(Status.OK);
                response.setBody(serializer.serialize(deals));
            } else {
                response.setHttpStatus(Status.NO_CONTENT);
            }
        } else {
            response.setHttpStatus(Status.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }
        return response;
    }

    private Response deleteDeal(Request request) {
        String username = request.getUsername();
        String token = request.getToken();

        if(tradingRepo.authenticate(username, token)) {
            String dealId = request.getTradingId();

            if (tradingRepo.dealExists(dealId)) {
                if(tradingRepo.deleteDeal(dealId, username)) {
                    response.setHttpStatus(Status.OK);
                    response.setBody("Trading deal successfully deleted");
                } else {
                    response.setHttpStatus(Status.FORBIDDEN);
                    response.setBody("The deal contains a card that is not owned by the user.");
                }
            } else {
                response.setHttpStatus(Status.NOT_FOUND);
                response.setBody("The provided deal ID was not found");
            }
        } else {
            response.setHttpStatus(Status.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }
        return response;
    }
}
