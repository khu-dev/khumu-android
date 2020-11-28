package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LikeComment {
    private int commentID;

    @JsonCreator
    public LikeComment(
            @JsonProperty("comment") int commentID) {
        this.commentID = commentID;
    }
    @JsonGetter("comment")
    public int getCommentID() {return commentID;}

    public void setCommentID(int commentID) { this.commentID = commentID;}
}
