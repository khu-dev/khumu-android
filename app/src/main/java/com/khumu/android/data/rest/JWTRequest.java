package com.khumu.android.data.rest;

import java.io.Serializable;

public class JWTRequest implements Serializable{
    public String username;
    public String password;

    public JWTRequest(
            String username,
            String password
    ) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
