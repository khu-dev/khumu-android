package com.khumu.android.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class ArticleTag implements Serializable {
    private String name;
    private boolean followed;

    @JsonCreator
    public ArticleTag (
            @JsonProperty("name") String name,
            @JsonProperty("followed") boolean followed) {
        this.name = name;
        this.followed = followed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }
}
