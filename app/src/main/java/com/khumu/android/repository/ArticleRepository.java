package com.khumu.android.repository;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Article;
import com.khumu.android.data.rest.ArticleListResponse;
import com.khumu.android.retrofitInterface.ArticleService;
import com.khumu.android.util.AuthenticationInterceptor;
import com.khumu.android.util.Util;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
public class ArticleRepository {
    private final static String TAG="ArticleRepository";

    @Inject
    public ArticleService service;
    @Inject
    public ArticleRepository(){}
    public List<Article> ListArticle() throws IOException, JSONException {
        return ListArticle(null, 1);
    }

    // board는 null | board_name. null인 경우에는 그냥 board 구분 없이, board_name을 입력하면 그 board의 게시물만.
    // page는 아직 pagination을 이용한 기능이 많지 않으므로 그냥 1 page 처리
    public List<Article> ListArticle(String board, int page) throws IOException, JSONException {
        // ArticleService에서 Articles들을 가져올 것이므로 getArticles와 파라미터로 토큰과 현재 페이지의 쿼리를 넘긴다.
        ArticleListResponse respString = new ArticleListResponse();
        if (board == null) {
            Call<ArticleListResponse> call = service.getArticles(page);
            respString = call.execute().body();
        } else {
            Call<ArticleListResponse> call = service.getArticles(board, page);
            respString = call.execute().body();
        }
        // String으로 받아온 것중 articles에 해당하는 "data" 값만 가져온다

        List<Article> articles = new ArrayList<>();
        if ( respString != null && respString.getData() != null){
            articles = respString.getData();
        };

        return articles;
    }
}
