package com.khumu.android.data;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArticleListResponse {

    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("data")
    @Expose
    private List<ArticleDTO> data = null;

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<ArticleDTO> getData() {
        return data;
    }

    public void setData(List<ArticleDTO> data) {
        this.data = data;
    }

}
