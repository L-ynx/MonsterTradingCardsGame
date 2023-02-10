package org.fhtw.application.controller.packages;

import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;

public class TransactionController implements Controller {
    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("POST"))
            return acquirePackage();

        return response;
    }

    private Response acquirePackage() {
        return response;
    }
}
