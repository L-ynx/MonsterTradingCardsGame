package org.fhtw.application.controller.trading;

import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;

public class TradingController implements Controller {
    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("GET"))
            return retrieveDeals();
        if (request.getMethod().equals("POST"))
            return createDeal();

        return null;
    }

    private Response createDeal() {
        return null;
    }

    private Response retrieveDeals() {
        return null;
    }
}
