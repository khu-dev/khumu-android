package com.khumu.android.data;

import com.auth0.android.jwt.JWT;

public class KhumuJWT {
    private JWT jwt;
    private String username;
    private String nickname;

    public KhumuJWT(String tokenStr) {
        this.jwt = new JWT(tokenStr);
        this.username = this.jwt.getClaim("user_id").asString();
        this.nickname = this.jwt.getClaim("nickname").asString();
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

    public String toString(){
        return this.jwt.toString();
    }
}
