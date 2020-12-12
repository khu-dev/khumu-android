package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;

public class Comment {
    private int id;
    private int articleID;
    private SimpleUser author;
    private String content;
    private int parent;
    private ArrayList<Comment> children;
    private int likeCommentCount;
    private boolean liked;
    private String commentCreatedAt;


    @JsonCreator
    public Comment (
        @JsonProperty("id") int id,
        @JsonProperty("articleID") int articleID,
        @JsonProperty("author") SimpleUser author,
        @JsonProperty("content") String content,
        @JsonProperty("parent") int parent,
        @JsonProperty("children") ArrayList<Comment> children,
        @JsonProperty("like_comment_count") int likeCommentCount,
        @JsonProperty("liked") boolean liked,
        @JsonProperty("created_at") String commentCreatedAt) {
        this.id = id;
        this.articleID = articleID;
        this.author = author;
        this.content = content;
        this.parent = parent;
        this.children = children;
        this.likeCommentCount = likeCommentCount;
        this.liked = liked;
        this.commentCreatedAt = commentCreatedAt;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }

    @JsonSetter("author")
    public SimpleUser getAuthor() {
        return author;
    }

    @JsonSetter("author")
    public void setAuthorUsername(SimpleUser author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getParent() { return parent; }

    public void setParent(int parent) { this.parent = parent; }

    public ArrayList<Comment> getChildren() { return children; }

    public void setChildren(ArrayList<Comment> children) { this.children = children; }

    public int getLikeCommentCount() { return likeCommentCount; }

    public void setLikeCommentCount(int likeCommentCount) { this.likeCommentCount = likeCommentCount; }

    public boolean isLiked() { return liked; }

    public void setLiked(boolean liked) { this.liked = liked; }

    public String getCommentCreatedAt() { return commentCreatedAt; }

    public void setCommentCreatedAt(String commentCreatedAt) { this.commentCreatedAt = commentCreatedAt; }
}