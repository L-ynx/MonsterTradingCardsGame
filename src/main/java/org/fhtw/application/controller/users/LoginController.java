package org.fhtw.application.controller.users;

import org.fhtw.application.model.Credentials;
import org.fhtw.application.repository.UserRepository;
import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;
import org.fhtw.http.Status;

public class LoginController implements Controller {
    public LoginController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    private final UserRepository userRepo;
    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("POST"))
            return loginUser(request.getBodyAs(Credentials.class));

        response.setHttpStatus(Status.BAD_REQUEST);

        return response;
    }

    private Response loginUser(Credentials cred) {
        String token;
        if ((token = userRepo.loginUser(cred.getUsername(), cred.getPassword())) != null) {
            response.setHttpStatus(Status.OK);
            response.setContentType("application/json");
            response.setBody(token);
        } else {
            response.setHttpStatus(Status.UNAUTHORIZED);
            response.setBody("Invalid username/password provided");
        }
        return response;
    }
}
