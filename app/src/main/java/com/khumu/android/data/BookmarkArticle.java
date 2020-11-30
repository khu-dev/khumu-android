package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BookmarkArticle {
    private int articleID;

    @JsonCreator
    public BookmarkArticle(
            @JsonProperty("article") int articleID) {
        this.articleID = articleID;
    }
    @JsonGetter("article")
    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }
}