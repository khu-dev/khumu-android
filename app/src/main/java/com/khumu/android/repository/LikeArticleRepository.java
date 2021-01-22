package com.khumu.android.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.android.KhumuApplication;
import com.khumu.android.data.LikeArticle;
import com.khumu.android.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

        HttpUrl.Builder urlBuilder = Util.newBuilder()
            .addPathSegment("articles")
            .addPathSegment(String.valueOf(likeArticle.getArticleID()))
            .addPathSegment("likes");

        Request toggleReq = new Request.Builder()
            .header("Authorization", "Bearer " + KhumuApplication.getToken())
            .patch(RequestBody.create(MediaType.parse("application/json"), "{}")) // 별 다른 body를 필요로 하지 않음.
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
