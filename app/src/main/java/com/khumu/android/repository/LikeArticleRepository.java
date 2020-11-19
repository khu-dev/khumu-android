package com.khumu.android.repository;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.android.data.Article;
import com.khumu.android.data.LikeArticle;
import com.khumu.android.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LikeArticleRepository {
    public LikeArticleRepository(){}
    public void toggleLikeArticle(LikeArticle likeArticle) throws IOException, JSONException {
        TokenRepository tokenRepo = new TokenRepository();
        String token = "";
        try{
            token = tokenRepo.GetToken(Util.DEFAULT_USERNAME, Util.DEFAULT_PASSWORD);
        } catch (Exception e){
            //
        }

        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        String likeArticleString = mapper.writeValueAsString(likeArticle);
//        JSONObject articleJSON = new JSONObject(articleString);
        //System.out.println(articleString);
        Request createReq = new Request.Builder()
                .header("Authorization", "Bearer "+token)
                .put(RequestBody.create(MediaType.parse("application/json"), likeArticleString))
                .url(Util.APIRootEndpoint + "like-articles")
                .build();
        Response createResp = client.newCall(createReq).execute();
    }
}
