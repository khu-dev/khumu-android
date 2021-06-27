package com.khumu.android.data.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceSubscriptionData {

    @SerializedName("resource_id")
    @Expose
    private int resourceId;
    @SerializedName("resource_kind")
    @Expose
    private String resourceKind;
    private String subscriber;
    @SerializedName("is_activated")
    @Expose
    public boolean isActivated;
}
