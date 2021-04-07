package com.khumu.android.data.rest;

import com.google.gson.annotations.SerializedName;
import com.khumu.android.data.Article;

public class ArticleResponse {
    @SerializedName("data")
    public Article article;

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
