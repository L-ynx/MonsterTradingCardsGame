package org.fhtw.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Card {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Name")
    private String cardName;
    @JsonProperty("Damage")
    private float damage;
    private String element_type;

    public Card(){}
    public Card(String id, String cardName, float damage) {
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

    public float getDamage() {
        return damage;
    }

    public String getElement_type() {
        return element_type;
    }

    public void setElement_type(String element_type) {
        this.element_type = element_type;
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
