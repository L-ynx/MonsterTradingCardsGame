package org.fhtw.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Card {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Name")
    private String cardName;
    @JsonProperty("Damage")
    private double damage;

    public Card(){}
    public Card(String id, String cardName, double damage) {
        this.id = id;
        this.cardName = cardName;
        this.damage = damage;
    }

    public String getId() {
        return id;
    }

    public String getCardName() {
        return cardName;
    }

    public double getDamage() {
        return damage;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }
}
