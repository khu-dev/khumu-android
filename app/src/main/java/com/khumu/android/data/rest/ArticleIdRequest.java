package com.khumu.android.data.rest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleIdRequest implements Serializable {
    @SerializedName("article")
    public String articleId;
}
