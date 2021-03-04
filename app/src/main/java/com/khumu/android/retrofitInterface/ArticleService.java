package com.khumu.android.retrofitInterface;

import com.khumu.android.data.rest.ArticleListResponse;
import com.khumu.android.data.Article;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleService {
    @GET("articles")
    Call<ArticleListResponse> getArticles(@Query("page") int page);

    @GET("articles")
    Call<ArticleListResponse> getArticles(@Query("board") String board, @Query("page") int page);

    @POST("articles")
    Call<Article> createArticle(@Header("Content-Type") String contentType, @Body Article article);

    @PATCH("articles/{id}")
    Call<Article> updateArticle(@Header("Content-Type") String contentType, @Path("id") int id, @Body Article article);

    @DELETE("articles/{id}")
    Call<Void> deleteArticle(@Header("Content-Type") String contentType, @Path("id") int id);
}

