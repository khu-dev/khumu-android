package com.khumu.android.data;

import com.google.gson.annotations.Expose;

public class SimpleComment {
    @Expose
    private int article;
    @Expose
    private String content;
    @Expose
    private String kind;
    @Expose
    private Integer parent;

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

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }
}