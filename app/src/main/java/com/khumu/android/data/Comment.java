package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Comment {
    @Expose
    private int id;
    @SerializedName("article")
    @Expose
    private int articleID;
    @Expose
    private SimpleUser author;
    @Expose
    private String content;
    @Expose
    private int parent;
    @Expose
    private String kind;
    @Expose
    private ArrayList<Comment> children;
    @SerializedName("like_comment_count")
    @Expose
    private int likeCommentCount;
    @Expose
    private boolean liked;
    @SerializedName("comment_created_at")
    @Expose
    private String commentCreatedAt;
    @SerializedName("is_author")
    @Expose
    private boolean isAuthor;

    public Comment () {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }

    public SimpleUser getAuthor() {
        return author;
    }

    public void setAuthor(SimpleUser author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public ArrayList<Comment> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Comment> children) {
        this.children = children;
    }

    public int getLikeCommentCount() {
        return likeCommentCount;
    }

    public void setLikeCommentCount(int likeCommentCount) {
        this.likeCommentCount = likeCommentCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getCommentCreatedAt() {
        return commentCreatedAt;
    }

    public void setCommentCreatedAt(String commentCreatedAt) {
        this.commentCreatedAt = commentCreatedAt;
    }

    public boolean isAuthor() {
        return isAuthor;
    }

    public void setIsAuthor(boolean isAuthor) {
        this.isAuthor = isAuthor;
    }
}