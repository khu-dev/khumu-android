package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Board {
    public String name;
    public String displayName;
    public String description;
    public List<Article> recentArticles;

    @JsonCreator
    public Board(
            @JsonProperty("name") String name,
            @JsonProperty("display_name") String displayName,
            @JsonProperty("description") String description,
            @JsonProperty("recent_articles") List<Article>recentArticles ) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.recentArticles = recentArticles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Article> getRecentArticles() {
        return recentArticles;
    }

    public void setRecentArticles(List<Article> recentArticles) {
        this.recentArticles = recentArticles;
    }
}
