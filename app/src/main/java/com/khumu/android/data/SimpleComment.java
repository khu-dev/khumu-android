package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

public class SimpleComment {
    @Expose
    private int article;
    @Expose
    private String content;

    public SimpleComment (
            int article,
            String content) {
        this.article = article;
        this.content = content;
    }

    public int getArticle() { return article; }

    public void setArticle(int article) { this.article = article; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }
}