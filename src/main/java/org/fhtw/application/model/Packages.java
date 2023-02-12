package org.fhtw.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Packages {
    private List<Card> cards;

    public Packages() {}

    public Packages(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }
}
