package org.fhtw.application.controller.users;

import org.fhtw.application.database.dbConnection;
import org.fhtw.application.model.Credentials;
import org.fhtw.application.repository.Repository;
import org.fhtw.application.repository.UserRepository;
import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;
import org.fhtw.http.Status;

import java.sql.Connection;

public class UserController implements Controller {
    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    private final UserRepository userRepo;
    @Override
    public Response process(Request request) {
        switch (request.getMethod()) {
            case "GET":
                return getUserData(request);
            case "POST":
                return registerUser(request);
            case "PUT":
                return updateUser(request);
        }

        response.setHttpStatus(Status.BAD_REQUEST);

        return response;
    }


    private Response updateUser(Request request) {
        response.setHttpStatus(Status.CREATED);
        return response;
    }

    private Response registerUser(Request request) {
        Credentials cred = request.getBodyAs(Credentials.class);

        if (userRepo.findUser(cred.getUsername()) == null) {
            if (userRepo.createUser(cred.getUsername(), cred.getPassword())) {
                response.setHttpStatus(Status.CREATED);
                response.setBody("User created");
            }
        } else {
            response.setHttpStatus(Status.CONFLICT);
            response.setBody("User with same username already registered");
        }

        return response;
    }


    private Response getUserData(Request request) {
        response.setHttpStatus(Status.OK);
        return response;
    }
}
