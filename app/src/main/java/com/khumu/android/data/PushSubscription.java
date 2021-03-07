package com.khumu.android.data;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class PushSubscription {
    @SerializedName("device_token")
    private String deviceToken;

}