package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleComment {
    private int article;
    private String content;

    @JsonCreator
    public SimpleComment (
        @JsonProperty("article") int article,
        @JsonProperty("content") String content) {
        this.article = article;
        this.content = content;
    }

    public int getArticle() { return article; }

    public void setArticle(int article) { this.article = article; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }
}
