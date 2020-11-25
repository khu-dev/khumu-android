package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class SimpleUser {
    private String username;
    private String nickname;
    private String state;

    // private int isParent;
    @JsonCreator
    public SimpleUser(
            @JsonProperty("username") String username,
            @JsonProperty("nickname") String nickname,
            @JsonProperty("state") String state) {
        this.username = username;
        this.nickname = nickname;
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}