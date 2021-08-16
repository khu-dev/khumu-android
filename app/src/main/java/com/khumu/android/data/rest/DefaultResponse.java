package com.khumu.android.data.rest;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultResponse<T> {
    @SerializedName("data")
    T data;
    @SerializedName("message")
    String message;
}
