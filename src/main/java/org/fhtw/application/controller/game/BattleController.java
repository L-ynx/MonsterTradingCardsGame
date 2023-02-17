package org.fhtw.application.controller.game;

import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;
import org.fhtw.http.Status;

import java.util.ArrayList;
import java.util.List;


public class BattleController implements Controller {
    private List<String> lobby = new ArrayList<>();
    private final int TIMEOUT = 60;

    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("POST"))
            return battle(request);

        return response;
    }

    private synchronized Response battle(Request request) {
        String username = request.getUsername();
        lobby.add(username);
        String user1 = lobby.get(0);
        System.out.println(lobby.size());

        if (lobby.size() == 1) {
            System.out.println("Waiting for opponent...");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return response;
        }

        if (lobby.size() == 2) {
            String user2 = lobby.get(1);
            // TODO: BATTLE LOGIC
            System.out.println("BATTLE " + user1 + " AND " + user2);

            notify();

            lobby.remove(user1);
            lobby.remove(user2);

            response.setHttpStatus(Status.OK);
            response.setBody("Battle has been carried out");
        }

        return response;
    }
}
