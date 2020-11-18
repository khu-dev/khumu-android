package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class SimpleUser {
    private String username;
    private String state;

    // private int isParent;
    @JsonCreator
    public SimpleUser(
            @JsonProperty("username") String username,
            @JsonProperty("state") String state) {
        this.username = username;
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}