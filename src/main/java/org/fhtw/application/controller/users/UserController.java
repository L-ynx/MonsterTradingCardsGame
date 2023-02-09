package org.fhtw.application.controller.users;

import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;
import org.fhtw.http.Status;

public class UserController implements Controller {

    @Override
    public Response process(Request request) {
        switch (request.getMethod()) {
            case "GET":
                return userData();
            case "POST":
                return registerUser();
            case "PUT":
                return updateUser();
        }

        response.setHttpStatus(Status.BAD_REQUEST);

        return response;
    }


    private Response updateUser() {
        response.setHttpStatus(Status.CREATED);
        return response;
    }

    private Response registerUser() {
        response.setHttpStatus(Status.OK);
        return response;
    }


    private Response userData() {
        response.setHttpStatus(Status.OK);
        return response;
    }
}
