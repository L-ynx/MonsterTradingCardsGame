package org.fhtw.application.controller.game;

import org.fhtw.application.misc.Lobby;
import org.fhtw.application.router.Controller;
import org.fhtw.http.Request;
import org.fhtw.http.Response;

public class BattleController implements Controller {
    private Lobby lobby = new Lobby();

    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("POST"))
            return battle(request);

        return response;
    }

    private Response battle(Request request) {
        String username = request.getUsername();
       /* lobby.addUser(username);
        String[] users = lobby.getWaitingUsers();
        String user1 = users[0];
        String user2 = users[1];
        System.out.println("BATTLE " + user1 + " AND " + user2);
        lobby.removeUsers(user1, user2); */

        return response;
    }
}
