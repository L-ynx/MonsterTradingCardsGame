package org.fhtw.application.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Trade {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("CardToTrade")
    private String cardToTrade;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("minimumDamage")
    private int minDamage;
    @JsonProperty("Element")
    private String element;

    public Trade() {
    }
    public Trade(String id, String cardToTrade, String type, int minDamage, String element) {
        this.id = id;
        this.cardToTrade = cardToTrade;
        this.type = type;
        this.minDamage = minDamage;
        this.element = element;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardToTrade() {
        return cardToTrade;
    }

    public void setCardToTrade(String cardToTrade) {
        this.cardToTrade = cardToTrade;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMinDamage() {
        return minDamage;
    }

    public void setMinDamage(int minDamage) {
        this.minDamage = minDamage;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }
}
