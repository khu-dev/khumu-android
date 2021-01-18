package com.khumu.android.retrofitInterface;

import com.khumu.android.data.ArticleDTO;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ArticleService {
    @GET("articles")
    Call<ResponseBody> getArticles(@Query("page") String page);
}

