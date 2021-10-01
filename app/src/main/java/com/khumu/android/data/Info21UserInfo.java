package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@JsonSerialize
@Data
@Builder
public class Info21UserInfo implements Serializable {
    String name;
    @SerializedName("student_num")
    String studentNum;
    String dept;
    Boolean verified;
}
