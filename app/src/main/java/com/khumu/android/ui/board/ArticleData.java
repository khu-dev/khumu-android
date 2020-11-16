package com.khumu.android.ui.board;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ArticleData {
    private String id;
    private String authorUsername;
    private String title;
    private String content;
    private String commentCount;

    public ArticleData(String id, String authorUsername, String title, String content, String commentCount) {
        this.id = id;
        this.authorUsername = authorUsername;
        this.title = title;
        this.content = content;
        this.commentCount = commentCount;
    }

    public String getID() {
        return id;
    }
    public void setID(String id) {
        this.id = id;
    }
    @JsonGetter("author")
    public String getAuthorUsername(){
//        return String.format("{\"id\": \"%s\", \"username\": \"%s\"}", authorUsername,authorUsername);
        return authorUsername;
    }

    @JsonSetter("author")
    public void setAuthorUsername(String authorUsername){this.authorUsername = authorUsername;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }
}
