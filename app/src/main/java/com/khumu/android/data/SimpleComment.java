package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleComment {
    private String content;

    @JsonCreator
    public SimpleComment (
        @JsonProperty("content") String content) {
        this.content = content;
    }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }
}
