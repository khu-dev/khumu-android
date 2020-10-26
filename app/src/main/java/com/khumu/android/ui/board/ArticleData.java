package com.khumu.android.ui.board;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ArticleData {
    private String pk;
    private String authorUsername;
    private String title;
    private String content;
    private String commentCount;

    public ArticleData(String pk, String authorUsername, String title, String content, String commentCount) {
        this.pk = pk;
        this.authorUsername = authorUsername;
        this.title = title;
        this.content = content;
        this.commentCount = commentCount;
    }

    public String getPk() {
        return pk;
    }
    public void setPk(String pk) {
        this.pk = pk;
    }
    @JsonGetter("author")
    public String getAuthorUsername(){
//        return String.format("{\"pk\": \"%s\", \"username\": \"%s\"}", authorUsername,authorUsername);
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
