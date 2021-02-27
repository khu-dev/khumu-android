package com.khumu.android.data;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PushSubscription {
    @SerializedName("device_token")
    private String deviceToken;
}