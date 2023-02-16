package org.fhtw.application.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Lobby {
    private ConcurrentLinkedQueue<String> waitingUsers = new ConcurrentLinkedQueue<>();

    public void addUser(String username) {
        waitingUsers.offer(username);
    }

    public String[] getWaitingUsers() {
        String[] users = new String[2];
        users[0] = waitingUsers.poll();
        users[1] = waitingUsers.poll();
        return users;
    }

    public void removeUsers(String user1, String user2) {
        waitingUsers.remove(user1);
        waitingUsers.remove(user2);
    }
}
