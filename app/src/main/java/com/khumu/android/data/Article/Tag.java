package com.khumu.android.data.Article;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tag {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("followed")
    @Expose
    private Boolean followed;

    public Tag(String name, boolean followed) {
        this.name = name;
        this.followed = followed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getFollowed() {
        return followed;
    }

    public void setFollowed(Boolean followed) {
        this.followed = followed;
    }

}