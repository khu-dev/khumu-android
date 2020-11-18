package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonSetter;

public class Comment {
    private String id;
    private String articleID;
    private String authorUsername;
    private String content;
    // private int isParent;

    public Comment(String authorString, String content, String id, String articleID) {
        this.id = id;
        this.articleID = articleID;
        this.authorUsername = authorString;
        this.content = content;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }

    @JsonSetter("author")
    public String getAuthorUsername() {
        return authorUsername;
    }

    @JsonSetter("author")
    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}