package com.khumu.android.data;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@JsonSerialize
@Data
@Builder
public class Info21UserInfo implements Serializable {
    String name;
    String StudentNum;
    String Dept;
    Boolean verified;
}
