package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.text.SimpleDateFormat;

public class Article {
    private int id;
    private SimpleUser author;
    private String title;
    private String board;
    private String content;
    private String kind;
    private int commentCount;
    private int likeArticleCount;
    private boolean liked;
    private String articleCreatedAt;

    public Article(String board, String title, String content, String kind) {
        this.board = board;
        this.title = title;
        this.content = content;
        this.kind = kind;
    }
    @JsonCreator
    public Article (
            @JsonProperty("id") int id,
            @JsonProperty("author") SimpleUser author,
            @JsonProperty("title") String title,
            @JsonProperty("board") String board,
            @JsonProperty("content") String content,
            @JsonProperty("kind") String kind,
            @JsonProperty("liked") boolean liked,
            @JsonProperty("comment_count") int commentCount,
            @JsonProperty("like_article_count") int likeArticleCount,
            @JsonProperty("created_at") String articleCreatedAt) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.board = board;
        this.content = content;
        this.kind = kind;
        this.liked = liked;
        this.commentCount = commentCount;
        this.likeArticleCount = likeArticleCount;
        this.articleCreatedAt = articleCreatedAt;
    }

//    @JsonCreator
//    public Article JSONSimpleArticle(
//            @JsonProperty("board") String board,
//            @JsonProperty("title") String title,
//            @JsonProperty("content") String content,
//            @JsonProperty("kind") String kind) {
//        return new Article(board, title, content, kind);
//    }

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

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
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

    public String getArticleCreatedAt() { return articleCreatedAt; }

    public void setArticleCreatedAt(String articleCreatedAt) { this.articleCreatedAt = articleCreatedAt; }
}
