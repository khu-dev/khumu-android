package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.text.SimpleDateFormat;

// 불필요한 내용은 빼고 사용하는 Article
// 예를 들면 Article 에서 Write할 때에는 넣지 않아도 되는 값 제외
// 근데 이 방식은 별로 안 좋은 것 같으니, Article에서 다양하게 field를 조절할 수 있게끔하는 게 나은 듯..?
public class SimpleArticle {
    private String board;
    private String title;
    private String content;
    private String kind;

    @JsonCreator
    public SimpleArticle(
            @JsonProperty("board") String board,
            @JsonProperty("title") String title,
            @JsonProperty("content") String content,
            @JsonProperty("kind") String kind) {
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
}
