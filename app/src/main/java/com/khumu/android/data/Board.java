package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.khumu.android.data.article.Article;

import java.io.Serializable;
import java.util.List;

public class Board implements Serializable {
    public String name;
    public String category;
    public String displayName;
    public String description;
    public Boolean followed;
    public String followedAt;
    public List<Article> recentArticles;

    @JsonCreator
    public Board(
            @JsonProperty("name") String name,
            @JsonProperty("category") String category,
            @JsonProperty("display_name") String displayName,
            @JsonProperty("description") String description,
            @JsonProperty("followed") Boolean followed,
            @JsonProperty("followed_at") String followedAt,
            @JsonProperty("recent_articles") List<Article>recentArticles ) {
        this.name = name;
        this.category = category;
        this.displayName = displayName;
        this.description = description;
        this.followed = followed;
        this.followedAt = followedAt;
        this.recentArticles = recentArticles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
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

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getFollowed() {
        return followed;
    }

    public void setFollowed(Boolean followed) {
        this.followed = followed;
    }

    public String getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(String followedAt) {
        this.followedAt = followedAt;
    }
}
