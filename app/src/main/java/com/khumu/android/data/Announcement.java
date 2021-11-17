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
    public String title;
    @SerializedName("sub_link")
    public String subLink;
    @SerializedName("date")
    public String createdAt;
    @SerializedName("authorDTO")
    public AnnouncementAuthor author;

    @Builder
    @Getter
    @Setter
    public static class AnnouncementAuthor {
        private Integer id;
        @SerializedName("author_name")
        public String authorName;
        public boolean followed;
    }
}