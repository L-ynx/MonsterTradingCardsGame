package org.fhtw.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Stats {
    @JsonProperty("Name")
    private String username;
    @JsonProperty("Games played")
    private int totalGames;
    @JsonProperty("Wins")
    private int gamesWon;
    @JsonProperty("Losses")
    private int gamesLost;
    @JsonProperty("Elo")
    private int elo;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }
}
