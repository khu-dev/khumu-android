package com.khumu.android.retrofitInterface;

import com.khumu.android.data.ArticleDTO;
import com.khumu.android.data.ArticleListResponse;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ArticleService {
    @GET("articles")
    Call<ArticleListResponse> getArticles(@Header("Authorization") String authorization, @Query("page") String page);
}

