package com.khumu.android.repository;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Article;
import com.khumu.android.data.KhumuUser;
import com.khumu.android.data.SimpleArticle;
import com.khumu.android.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
@Module
public class ArticleRepository {
    private final static String TAG="ArticleRepository";
    @Inject
    public ArticleRepository(){}
    public ArrayList<Article> ListArticle() throws IOException, JSONException {
        return ListArticle(null, 1);
    }

    // board는 null | board_name. null인 경우에는 그냥 board 구분 없이, board_name을 입력하면 그 board의 게시물만.
    // page는 아직 pagination을 이용한 기능이 많지 않으므로 그냥 1 page 처리
    public ArrayList<Article> ListArticle(String board, int page) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = Util.newBuilder()
            .addPathSegment("articles");
        urlBuilder = urlBuilder.addQueryParameter("page", String.valueOf(page));
        if (board != null){
            urlBuilder = urlBuilder.addQueryParameter("board", board);
        }
        Request req = new Request.Builder()
            .header("Authorization", "Bearer "+ KhumuApplication.getToken())
            .url(urlBuilder.build())
            .build();

        Response fetchResp = client.newCall(req).execute();
        String respString = fetchResp.body().string();
        // String으로 받아온 것중 articles에 해당하는 "data" 값만 가져온다
        String data = new JSONObject(respString).getString("data");
//        Log.d(TAG, "ListArticle: "+data);
        JSONArray articleJSONArray = new JSONArray(data);
        ArrayList<Article> articles = new ArrayList<>();

        ObjectMapper mapper  = new ObjectMapper();
        // 이걸 해야 정의하지 않은 property가 있어도 에러가 안남.
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        for(int i=0; i < articleJSONArray.length(); i++){
            Article article = mapper.readValue(articleJSONArray.getJSONObject(i).toString(), Article.class);
            articles.add(article);
        }
        return articles;
    }

    public boolean CreateArticle(SimpleArticle article) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        String articleStr = mapper.writeValueAsString(article);
        System.out.println(articleStr);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), articleStr);

        HttpUrl.Builder urlBuilder = Util.newBuilder()
                .addPathSegment("articles");

        Request req = new Request.Builder()
                .header("Authorization", "Bearer " + KhumuApplication.getToken())
                .post(body)
                .url(urlBuilder.build())
                .build();

        Response resp = client.newCall(req).execute();

        if(resp.code() == 201){
            return true;
        } else{
            return false;
        }

    }
}
