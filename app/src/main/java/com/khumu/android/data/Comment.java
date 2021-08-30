package com.khumu.android.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comment {
    private int id;
    @SerializedName("article")
    private int articleID;
    @SerializedName("study_article")
    private int studyArticleID;
    @Expose
    private SimpleUser author;
    @Expose
    private String content;
    @Expose
    private Integer parent;
    private String kind;
    @Expose
    private ArrayList<Comment> children;
    @SerializedName("like_comment_count")
    private int likeCommentCount;
    private boolean liked;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("is_author")
    private Boolean isAuthor;
    @SerializedName("is_written_by_article_author")
    private Boolean isWrittenByArticleAuthor;
}