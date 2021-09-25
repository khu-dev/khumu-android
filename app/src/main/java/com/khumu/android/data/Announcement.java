package com.khumu.android.data;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
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

    @Data
    @Builder
    @Getter
    @Setter
    public static class AnnouncementAuthor {
        public String name;
        public boolean followed;
    }
}