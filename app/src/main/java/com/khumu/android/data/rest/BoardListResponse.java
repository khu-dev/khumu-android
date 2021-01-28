package com.khumu.android.data.rest;

import com.google.gson.annotations.SerializedName;
import com.khumu.android.data.Board;

import java.util.List;

public class BoardListResponse {
    @SerializedName("data")
    private List<Board> data;

    public List<Board> getData() {
        return data;
    }

    public void setData(List<Board> data) {
        this.data = data;
    }
}
