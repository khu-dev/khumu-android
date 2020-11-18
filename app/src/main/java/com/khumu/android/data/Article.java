package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Article {
    private String id;
    private SimpleUser author;
    private String title;
    private String content;
    private String commentCount;

    @JsonCreator
    public Article(
        @JsonProperty("id") String id,
        @JsonProperty("author") SimpleUser author,
        @JsonProperty("title") String title,
        @JsonProperty("content") String content,
        @JsonProperty("comment_count") String commentCount) {
        this.id = id;
        this.author = author;
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
    public SimpleUser getAuthor(){
//        return String.format("{\"id\": \"%s\", \"username\": \"%s\"}", authorUsername,authorUsername);
        return author;
    }

    @JsonSetter("author")
    public void setAuthorUsername(SimpleUser author){this.author = author;}

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
