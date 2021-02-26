package com.khumu.android.data.rest;

import com.google.gson.annotations.SerializedName;
import com.khumu.android.data.Comment;

import java.util.List;

public class CommentListResponse {
    @SerializedName("data")
    private List<Comment> data;

    public List<Comment> getData() {
        return data;
    }

    public void setData(List<Comment> data) {
        this.data = data;
    }
}
