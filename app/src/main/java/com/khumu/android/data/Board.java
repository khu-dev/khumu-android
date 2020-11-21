package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Board {
    public String name;
    public String shortName;
    public String longName;
    public String description;
    public List<Article> recentArticles;

    @JsonCreator
    public Board(
            @JsonProperty("name") String name,
            @JsonProperty("short_name") String shortName,
            @JsonProperty("long_name") String longName,
            @JsonProperty("description") String description,
            @JsonProperty("recent_articles") List<Article>recentArticles ) {
        this.name = name;
        this.shortName = shortName;
        this.longName = longName;
        this.description = description;
        this.recentArticles = recentArticles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
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
