package com.khumu.android.data.rest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportObjectRequest implements Serializable {
    @SerializedName("resource_kind")
    public String resourceKind;

    @SerializedName("resource_id")
    public String resourceId;
}
