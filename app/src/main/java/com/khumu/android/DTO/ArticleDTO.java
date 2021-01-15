package com.khumu.android.DTO;

import com.google.gson.annotations.SerializedName;
import com.khumu.android.data.SimpleUser;

public class ArticleDTO {
    @SerializedName("id")
    private int id;

    @SerializedName("author")
    private SimpleUser author;

    @SerializedName("title")
    private String title ;

    @SerializedName("board")
    private String board;

    @SerializedName("content")
    private String content;

    @SerializedName("kind")
    private String kind;

    @SerializedName("comment_count")
    private int commentCount;

    @SerializedName("created_at")
    private String articleCreatedAt;

    @SerializedName("liked")
    private boolean liked;

    @SerializedName("like_article_count")
    private int likeArticleCount;

    @SerializedName("bookmarked")
    private boolean bookmarked;

    @SerializedName("bookmark_article_count")
    private int bookmarkArticleCount;

    public ArticleDTO(String board, String title, String content, String kind) {
        this.board = board;
        this.title = title;
        this.content = content;
        this.kind = kind;
    }

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

    public int getBookmarkArticleCount() {
        return bookmarkArticleCount;
    }

    public void setBookmarkArticleCount(int bookmarkArticleCount) {
        this.bookmarkArticleCount = bookmarkArticleCount;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getArticleCreatedAt() { return articleCreatedAt; }

    public void setArticleCreatedAt(String articleCreatedAt) { this.articleCreatedAt = articleCreatedAt; }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", author=" + author +
                ", title=" + title +
                ", board=" + board +
                ", content=" + content +
                ", kind=" + kind +
                ", comment_count=" + commentCount +
                ", created_at=" + articleCreatedAt +
                ", liked=" + liked +
                ", like_article_count=" + likeArticleCount +
                ", bookmarked=" + bookmarked +
                ", bookmark_article_count=" + bookmarkArticleCount +
                '}';
    }
}
