package com.khumu.android.data.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khumu.android.data.StudyArticle;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyListResponse {
    @SerializedName("links")
    @Expose(serialize = false, deserialize = false)
    private Links links;
    private Integer count;
    @SerializedName("data")
    private List<StudyArticle> data;
}
