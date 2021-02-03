package com.khumu.android.retrofitInterface;

import com.khumu.android.data.rest.ArticleListResponse;
import com.khumu.android.data.Article;

import dagger.Module;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
@Module
public interface ArticleService {
    @GET("articles")
    Call<ArticleListResponse> getArticles(@Header("Authorization") String authorization, @Query("page") String page);

    @GET("articles")
    Call<ArticleListResponse> getArticles(@Header("Authorization") String authorization, @Query("page") String page, @Query("board") String board);

    @POST("articles")
    Call<Article> createArticle(@Header("Authorization") String authorization, @Header("Content-Type") String contentType, @Body Article article);

    @PUT("articles/{id}")
    Call<Article> updateArticle(@Header("Authorization") String authorization, @Header("Content-Type") String contentType, @Path("id") String id, @Body Article article);

    @DELETE("articles/{id}")
    Call<Article> deleteArticle(@Header("Authorization") String authorization, @Header("Content-Type") String contentType, @Path("id") String id);



}

