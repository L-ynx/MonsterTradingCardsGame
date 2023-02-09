package org.fhtw.application.controller.users;

import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;
import org.fhtw.http.Status;

public class LoginController implements Controller {
    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("POST"))
            return loginUser();

        response.setHttpStatus(Status.BAD_REQUEST);

        return response;
    }

    private Response loginUser() {
        return response;
    }
}
