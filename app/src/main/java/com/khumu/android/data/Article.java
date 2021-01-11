package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Article implements Serializable {
    private int id;
    private SimpleUser author;
    private String title;
    private String boardName;
    private String boardDisplayName;
    private String content;
    private String kind;
    private List<ArticleTag> tags;
    private int commentCount;
    private int likeArticleCount;
    private int bookmarkArticleCount;
    private boolean liked;
    private boolean bookmarked;
    private String articleCreatedAt;

    // 최소한 ArticleWrite할 때 사용
    // 빈 Article 생성 후 내용을 수정해나감.
    public Article(){
        tags = new ArrayList<ArticleTag>();
    }

    @JsonCreator
    public Article (
            @JsonProperty("id") int id,
            @JsonProperty("author") SimpleUser author,
            @JsonProperty("title") String title,
            @JsonProperty("board_name") String boardName,
            @JsonProperty("board_display_name") String boardDisplayName,
            @JsonProperty("content") String content,
            @JsonProperty("kind") String kind,
            @JsonProperty("tags") List<ArticleTag> tags,
            @JsonProperty("liked") boolean liked,
            @JsonProperty("bookmarked") boolean bookmarked,
            @JsonProperty("comment_count") int commentCount,
            @JsonProperty("like_article_count") int likeArticleCount,
            @JsonProperty("bookmark_article_count") int bookmarkArticleCount,
            @JsonProperty("created_at") String articleCreatedAt) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.boardName = boardName;
        this.boardDisplayName = boardDisplayName;
        this.content = content;
        this.kind = kind;
        this.tags = tags;
        this.liked = liked;
        this.bookmarked = bookmarked;
        this.commentCount = commentCount;
        this.likeArticleCount = likeArticleCount;
        this.bookmarkArticleCount = bookmarkArticleCount;
        this.articleCreatedAt = articleCreatedAt;
    }
// 없어도 되네
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

    public SimpleUser getAuthor(){
        return author;
    }

    public void setAuthorUsername(SimpleUser author){this.author = author;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // 편의상 board를 object 형태가 아닌 board_name, board_display_name으로 이용하고있다.
    // 하지만 article create api 요청을 보낼 때에는 board: board_name의 string 값을 전달해야하기 때문에
    // board_name이 아닌 board에 대한 JsonGetter로 이용하기 위해 Annotation 추가.
    @JsonGetter("board")
    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getBoardDisplayName() {
        return boardDisplayName;
    }

    public void setBoardDisplayName(String boardDisplayName) {
        this.boardDisplayName = boardDisplayName;
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

    public List<ArticleTag> getTags() {
        return tags;
    }

    public void setTags(List<ArticleTag> tags) {
        this.tags = tags;
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
}
