package com.khumu.android.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleUser implements Serializable {

    private String username;
    private String nickname;
    private String status;

    // private int isParent;
    public SimpleUser(
            String username,
            String nickname,
            String state) {
        this.username = username;
        this.nickname = nickname;
        this.status = state;
    }
}