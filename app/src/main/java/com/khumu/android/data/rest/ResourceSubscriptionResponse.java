package com.khumu.android.data.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceSubscriptionResponse {
    private String message;
    private ResourceSubscriptionData data;
}

