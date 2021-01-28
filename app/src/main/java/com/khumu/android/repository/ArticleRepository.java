package com.khumu.android.repository;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Article;
import com.khumu.android.data.rest.ArticleListResponse;
import com.khumu.android.retrofitInterface.ArticleService;
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
    public ArrayList<Article> ListArticle() throws IOException, JSONException {
        return ListArticle(null, 1);
    }

    // board는 null | board_name. null인 경우에는 그냥 board 구분 없이, board_name을 입력하면 그 board의 게시물만.
    // page는 아직 pagination을 이용한 기능이 많지 않으므로 그냥 1 page 처리
    public ArrayList<Article> ListArticle(String board, int page) throws IOException, JSONException {
        // ArticleService에서 Articles들을 가져올 것이므로 getArticles와 파라미터로 토큰과 현재 페이지의 쿼리를 넘긴다.
        ArticleListResponse respString = new ArticleListResponse();
        if (board == null) {
            Call<ArticleListResponse> call = service.getArticles("Bearer " + KhumuApplication.getToken(), String.valueOf(page));
            respString = call.execute().body();
        } else {
            Call<ArticleListResponse> call = service.getArticles("Bearer " + KhumuApplication.getToken(), String.valueOf(page), board);
            respString = call.execute().body();
        }
        // String으로 받아온 것중 articles에 해당하는 "data" 값만 가져온다
        List<Article> ArticleList = respString.getData();
        System.out.println(ArticleList.toString());
        //String data = new JSONObject(respString).getString();
        Log.d(TAG, "ListArticle: "+ ArticleList.toString());

        ArrayList<Article> articles = new ArrayList<>();
        articles.addAll(ArticleList);

        return articles;
    }

    public boolean CreateArticle(Article article) throws IOException, JSONException {
        Call<Article> call = service.createArticle("Bearer " + KhumuApplication.getToken(), "application/json", article);
        retrofit2.Response<Article> resp = call.execute();
        if(resp.code() == 201){
            return true;
        } else{
            return false;
        }
    }

    public void UpdateArticle(Article article) throws Exception {
        Call<Article> call = service.updateArticle("Bearer " + KhumuApplication.getToken(), "application/json", String.valueOf(article.getId()), article);
        retrofit2.Response<Article> resp = call.execute();
        if(resp.code() == 201){
//            return true;
        } else{
//            return false;
        }
//        OkHttpClient client = new OkHttpClient();
//        ObjectMapper mapper = new ObjectMapper();
//        String articleStr = mapper.writeValueAsString(article);
//        System.out.println(articleStr);
//        RequestBody body = RequestBody.create(MediaType.parse("application/json"), articleStr);
//
//        HttpUrl.Builder urlBuilder = Util.newBuilder()
//                .addPathSegment("articles")
//                .addPathSegment(String.valueOf(article.getId()));
//
//        Request req = new Request.Builder()
//                .header("Authorization", "Bearer " + KhumuApplication.getToken())
//                .patch(body)
//                .url(urlBuilder.build())
//                .build();

//        Response resp = client.newCall(req).execute();
//        if(resp.code() != 200){
//            Log.d(TAG, "UpdateArticle: " + resp.body());
//            throw new Exception("게시물 업데이트 실패");
//        }
    }
    // 삭제가 잘 되었으면 true, 오류가 발생하면 false
    public boolean DeleteArticle(int articleID) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = Util.newBuilder()
                .addPathSegment("articles")
                .addPathSegment(String.valueOf(articleID));

        Request req = new Request.Builder()
                .header("Authorization", "Bearer " + KhumuApplication.getToken())
                .url(urlBuilder.build())
                .delete()
                .build();

        Response resp = null;
        try {
            resp = client.newCall(req).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if(resp.code() == 204){
            return true;
        } else{
            return false;
        }
    }
}
