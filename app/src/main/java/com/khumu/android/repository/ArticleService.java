package com.khumu.android.repository;

import androidx.annotation.Nullable;

import com.khumu.android.data.Article;
import com.khumu.android.data.rest.ArticleListResponse;
import com.khumu.android.data.rest.ArticleResponse;
import com.khumu.android.data.rest.DefaultResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleService {
    @GET("community/v1/articles")
    Call<ArticleListResponse> getArticles(@Query("page") int page);

    @GET("community/v1/articles")
    Call<ArticleListResponse> getArticles(@Query("board") String board, @Query("page") int page, @Query("size") int size);

    @GET("community/v1/articles")
    Call<ArticleListResponse> searchArticles(@Query("q") String q, @Nullable @Query("cursor") String cursor);

    @GET("community/v1/articles/{id}")
    Call<ArticleResponse> getArticle(@Path("id") int id);

    @POST("community/v1/articles")
    Call<Article> createArticle(@Header("Content-Type") String contentType, @Body Article article);

    @PATCH("community/v1/articles/{id}")
    Call<Article> updateArticle(@Header("Content-Type") String contentType, @Path("id") int id, @Body Article article);

    @DELETE("community/v1/articles/{id}")
    Call<Void> deleteArticle(@Header("Content-Type") String contentType, @Path("id") int id);

    @PATCH("community/v1/articles/{id}/likes")
    Call<DefaultResponse> likeArticleToggle(@Header("Content-Type") String contentType, @Path("id") int id);
    @PATCH("community/v1/articles/{id}/bookmarks")
    Call<DefaultResponse> bookmarkArticleToggle(@Header("Content-Type") String contentType, @Path("id") int id);


}

