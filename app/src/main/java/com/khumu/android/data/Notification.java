package com.khumu.android.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notification implements Serializable {
    private Long id;
    private String title;
    private String content;
    private String kind;
    private String recipient;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("is_read")
    private boolean isRead;

    public Notification(Long id) {
        this.id = 1L;
        this.title = "my title";
        this.content = "my content";
    }
}
