package com.khumu.android.repository;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.android.data.Article;
import com.khumu.android.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
@Module
public class ArticleRepository {
    @Inject
    public ArticleRepository(){}
    public ArrayList<Article> ListArticle() throws IOException, JSONException {
        TokenRepository tokenRepo = new TokenRepository();
        String token = "";
        try{
            token = tokenRepo.GetToken(Util.DEFAULT_USERNAME, Util.DEFAULT_PASSWORD);
        } catch (Exception e){
            //
        }

        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder()
                .header("Authorization", "Bearer "+ token)
                .url(Util.APIRootEndpoint + "articles")
                .build();
        Response fetchResp = client.newCall(req).execute();
        String respString = fetchResp.body().string();
        // String으로 받아온 것중 articles에 해당하는 "data" 값만 가져온다
//        System.out.println(respString);
        String data = new JSONObject(respString).getString("data");
        System.out.println(data);
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
}
