package com.khumu.android.repository;

import android.util.Log;

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
    private final static String TAG = "LikeArticleRepository";
    @Inject
    public LikeArticleRepository(){}
    public static class BadRequestException extends Exception{
        public BadRequestException(String message){
            super(message);
        }
    }
    public void toggleLikeArticle(LikeArticle likeArticle) throws IOException, JSONException, BadRequestException {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        String likeArticleString = mapper.writeValueAsString(likeArticle);
//        JSONObject articleJSON = new JSONObject(articleString);
        //System.out.println(articleString);

        HttpUrl.Builder urlBuilder = Util.newBuilder()
            .addPathSegment("like-articles");

        Request toggleReq = new Request.Builder()
            .header("Authorization", "Bearer " + KhumuApplication.getToken())
            .patch(RequestBody.create(MediaType.parse("application/json"), likeArticleString))
            .url(urlBuilder.build())
            .build();
        Response toggleResp = client.newCall(toggleReq).execute();

        if (toggleResp.code() != 201 && toggleResp.code() != 204 && toggleResp.code() != 200){
            String respString = toggleResp.body().string();
            JSONObject respObj = new JSONObject(respString);
            throw new BadRequestException(respObj.getString("message"));
        }

        // create or deleted
//        if (toggleResp.code() == 201 || toggleResp.code() == 204){
//            String errorMessage = "좋아요 기능을 성공적으로 수행하지 못했습니다.";
//            if( !respObj.isNull("message")) errorMessage = respObj.getString("message");
//            throw new BadRequestException(errorMessage);
//        }
    }
}
