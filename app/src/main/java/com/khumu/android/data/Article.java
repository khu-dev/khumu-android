package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Article {
    private int id;
    private SimpleUser author;
    private String title;
    private String content;
    private int commentCount;
    private int likeArticleCount;
    private boolean liked;

    @JsonCreator
    public Article(
        @JsonProperty("id") int id,
        @JsonProperty("author") SimpleUser author,
        @JsonProperty("title") String title,
        @JsonProperty("content") String content,
        @JsonProperty("liked") boolean liked,
        @JsonProperty("comment_count") int commentCount,
        @JsonProperty("like_article_count") int likeArticleCount) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.liked = liked;
        this.commentCount = commentCount;
        this.likeArticleCount = likeArticleCount;
    }

    public int getID() {
        return id;
    }
    public void setID(int id) {
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

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikeArticleCount() {
        return likeArticleCount;
    }

    public void setLikeArticleCount(int likeArticleCount) {
        this.likeArticleCount = likeArticleCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
