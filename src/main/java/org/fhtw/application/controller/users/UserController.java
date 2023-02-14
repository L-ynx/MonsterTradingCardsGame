package org.fhtw.application.controller.users;

import org.fhtw.application.database.dbConnection;
import org.fhtw.application.model.Credentials;
import org.fhtw.application.model.Profile;
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
        Profile profile = request.getBodyAs(Profile.class);
        String username = request.getUsername();
        String token = request.getToken();
        String pathUser = request.getPathUser();

        if (userRepo.authenticate(username, token)) {
            if (userRepo.updateUser(pathUser, profile)) {
                response.setHttpStatus(Status.OK);
                response.setBody("User successfully updated");
            } else {
                response.setHttpStatus(Status.NOT_FOUND);
                response.setBody("User not found");
            }
        } else {
            response.setHttpStatus(Status.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }

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
        String username = request.getUsername();
        String token = request.getToken();
        String pathUser = request.getPathUser();

        if (userRepo.authenticate(username, token) && (username.equals(pathUser) || username.equals("admin"))) {
            Profile userProfile = userRepo.getProfile(pathUser);

            if(userProfile != null) {
                System.out.println(userProfile.getName() + userProfile.getBio() + userProfile.getImage());
                response.setHttpStatus(Status.OK);
                response.setBody("Data successfully retrieved");
            } else {
                response.setHttpStatus(Status.NOT_FOUND);
                response.setBody("User not found");
            }
        } else {
            response.setHttpStatus(Status.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }

        return response;
    }
}
