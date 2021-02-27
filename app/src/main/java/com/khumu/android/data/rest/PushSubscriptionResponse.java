package com.khumu.android.data.rest;

import com.google.gson.annotations.SerializedName;
import com.khumu.android.data.PushSubscription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class PushSubscriptionResponse {
    private String message;
    private PushSubscription data;
}
