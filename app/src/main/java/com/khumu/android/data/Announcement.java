package com.khumu.android.data;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@JsonSerialize
@Data
@Builder
public class Announcement implements Serializable {
    Long id;
    String title;
    AnnouncementAuthor author;
    String referenceUrl;

    @JsonSerialize
    @Data
    @Builder
    public static class AnnouncementAuthor {
        String name;
        boolean followed;
    }
}
