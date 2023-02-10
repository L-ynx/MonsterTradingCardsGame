package org.fhtw.application.controller.packages;

import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;

public class PackagesController implements Controller {

    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("POST"))
            return createPackage();

        return response;
    }

    private Response createPackage() {
        return response;
    }
}
