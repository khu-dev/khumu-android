package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

// 현재는 Sign up 할 때에만 사용하기때문에 json => Java instance로의 변환은 구현하지않음.
public class KhumuUser implements Serializable {
    private String username;
    private String nickname;
    private String password;
    private String email;
    private String studentNumber;
    private String department;

    public KhumuUser(String username, String nickname, String password, String email,
                     @JsonProperty("student_number") String studentNumber, String department) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.studentNumber = studentNumber;
        this.department = department;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
