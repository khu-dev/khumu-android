package com.khumu.android.data.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khumu.android.data.Article;
import com.khumu.android.data.KhumuUser;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {
    @SerializedName("data")
    @Expose
    KhumuUser data;
    String message;
}
