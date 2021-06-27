package com.khumu.android.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResourceSubscription {
    @SerializedName("resource_kind")
    @Expose
    private String resourceKind;
    @SerializedName("resource_id")
    @Expose
    private int resourceId;
}
