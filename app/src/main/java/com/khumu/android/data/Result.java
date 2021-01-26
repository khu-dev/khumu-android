package com.khumu.android.data;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.khumu.android.data.article.Article;

public class Result {

    private Article request;
    private JsonObject response;

    public Article getRequest() {
        return request;
    }

    public void setRequest(Article request) {
        this.request = request;
    }

    public JsonObject getResponse() {
        return response;
    }

    public void setResponse(JsonObject response) {
        this.response = response;
    }
}