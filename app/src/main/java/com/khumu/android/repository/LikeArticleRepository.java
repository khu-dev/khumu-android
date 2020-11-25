package com.khumu.android.repository;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Article;
import com.khumu.android.data.LikeArticle;
import com.khumu.android.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.Module;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
@Module
public class LikeArticleRepository {
    @Inject
    public LikeArticleRepository(){}
    public void toggleLikeArticle(LikeArticle likeArticle) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        String likeArticleString = mapper.writeValueAsString(likeArticle);
//        JSONObject articleJSON = new JSONObject(articleString);
        //System.out.println(articleString);

        HttpUrl.Builder urlBuilder = Util.newBuilder()
            .addPathSegment("like-articles");

        Request createReq = new Request.Builder()
            .header("Authorization", "Bearer " + KhumuApplication.getToken())
            .put(RequestBody.create(MediaType.parse("application/json"), likeArticleString))
            .url(urlBuilder.build())
            .build();
        Response createResp = client.newCall(createReq).execute();
    }
}
