package org.fhtw.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Credentials {
    @JsonProperty("Username")
    private String username;
    @JsonProperty("Password")
    private String password;

    public Credentials() {}
    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
