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
    @GET("articles")
    Call<ArticleListResponse> getArticles(@Query("page") int page);

    @GET("articles")
    Call<ArticleListResponse> getArticles(@Query("board") String board, @Query("size") int size);

    @GET("articles")
    // 이미 django에서 url encoded 된 값을 전달해준다.
    Call<ArticleListResponse> getNextCursorArticles(@Query("board") String board, @Query(value = "cursor", encoded = true) String nextCursor, @Query("size") int size);

    @GET("articles")
    Call<ArticleListResponse> searchArticles(@Query("q") String q, @Nullable @Query("cursor") String cursor);

    @GET("articles/{id}")
    Call<ArticleResponse> getArticle(@Path("id") int id);

    @POST("articles")
    Call<Article> createArticle(@Header("Content-Type") String contentType, @Body Article article);

    @PATCH("articles/{id}")
    Call<Article> updateArticle(@Header("Content-Type") String contentType, @Path("id") int id, @Body Article article);

    @DELETE("articles/{id}")
    Call<Void> deleteArticle(@Header("Content-Type") String contentType, @Path("id") int id);

    @PATCH("articles/{id}/likes")
    Call<DefaultResponse> likeArticleToggle(@Header("Content-Type") String contentType, @Path("id") int id);
    @PATCH("articles/{id}/bookmarks")
    Call<DefaultResponse> bookmarkArticleToggle(@Header("Content-Type") String contentType, @Path("id") int id);


}

