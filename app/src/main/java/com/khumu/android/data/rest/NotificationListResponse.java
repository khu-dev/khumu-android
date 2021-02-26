package com.khumu.android.data.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khumu.android.data.Article;
import com.khumu.android.data.Notification;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationListResponse {
    private String message;
    private List<Notification> data;
}
