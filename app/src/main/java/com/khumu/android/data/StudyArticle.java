package com.khumu.android.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import retrofit2.http.GET;
import retrofit2.http.Query;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudyArticle implements Serializable {
    private Integer id;
    private String title;
    private SimpleUser author;
    private String numOfPeople;
    private String term;
    @SerializedName("study_method")
    @Expose
    private String studyMethod;
    @SerializedName("study_field")
    @Expose
    private String studyField;
    private String content;
    private List<String> images;
    private String createdAt;
    @SerializedName("is_author")
    @Expose
    private Boolean isAuthor;
    @SerializedName("comment_count")
    @Expose
    private Integer commentCount;
    private Boolean bookmarked;
    @SerializedName("study_field_name")
    @Expose
    private String studyFieldName;
}
