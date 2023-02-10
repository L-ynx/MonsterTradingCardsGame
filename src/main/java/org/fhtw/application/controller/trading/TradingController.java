package org.fhtw.application.controller.trading;

import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;

public class TradingController implements Controller {
    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("GET"))
            return retrieveDeals();
        if (request.getMethod().equals("POST")) {
            if (request.getPath().startsWith("/tradings/"))
                return trade();
            return createDeal();
        }

        return response;
    }

    private Response trade() {
        return response;
    }

    private Response createDeal() {
        return response;
    }

    private Response retrieveDeals() {
        return response;
    }
}
