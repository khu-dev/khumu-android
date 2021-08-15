package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 현재는 Sign up 할 때에만 사용하기때문에 json => Java instance로의 변환은 구현하지않음.
@NoArgsConstructor
@Getter
@Setter
@JsonSerialize
public class KhumuUser implements Serializable {
    private String username;
    private String nickname;
    private String password;
    private String email;
    private String studentNumber;
    private String department;
    private String kind;

    public KhumuUser(String username, String nickname, String password, String email,
                     @JsonProperty("student_number") String studentNumber, String department) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.studentNumber = studentNumber;
        this.department = department;
    }
}
