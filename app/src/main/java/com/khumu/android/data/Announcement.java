package com.khumu.android.data;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Announcement implements Serializable {
    private Integer id;
    private String title;
    private String subLink;
    @SerializedName("created_at")
    private String createdAt;
    public AnnouncementAuthor author;

    @Builder
    @Getter
    @Setter
    public static class AnnouncementAuthor {
        public Integer id;
        public String name;
        public boolean followed;
    }
}