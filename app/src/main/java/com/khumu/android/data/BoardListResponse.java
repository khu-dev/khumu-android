package com.khumu.android.data;

import com.google.gson.annotations.SerializedName;

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
