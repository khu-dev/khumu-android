package com.khumu.android.data.rest;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultResponse {
    @SerializedName("data")
    Object data;
    @SerializedName("message")
    String message;
}
