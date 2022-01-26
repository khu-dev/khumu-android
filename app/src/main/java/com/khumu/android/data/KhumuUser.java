package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 현재는 Sign up 할 때에만 사용하기때문에 json => Java instance로의 변환은 구현하지않음.
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonSerialize
@AllArgsConstructor
public class KhumuUser implements Serializable {
    @Setter(AccessLevel.NONE)
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    private String nickname;
    private String password;
    private String email;
    @SerializedName("student_number")
    private String studentNumber;

    private String department;
    private String kind;

    public KhumuUser(String username, String nickname, String password, String email,
                      String studentNumber, String department) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.studentNumber = studentNumber;
        this.department = department;
    }
}
