package com.khumu.android.retrofitInterface;

import com.khumu.android.data.ArticleListResponse;
import com.khumu.android.data.article.Article;

import dagger.Module;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
@Module
public interface ArticleService {
    @GET("articles")
    Call<ArticleListResponse> getArticles(@Header("Authorization") String authorization, @Query("page") String page);

    @GET("articles")
    Call<ArticleListResponse> getArticles(@Header("Authorization") String authorization, @Query("page") String page, @Query("board") String board);

    @POST("articles")
    Call<Article> createArticle(@Header("Authorization") String authorization, @Header("Content-Type") String contentType, @Body Article article);
}

