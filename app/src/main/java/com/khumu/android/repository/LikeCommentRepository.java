package com.khumu.android.repository;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.android.KhumuApplication;
import com.khumu.android.data.LikeComment;
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
public class LikeCommentRepository {
    private final static String TAG = "LikeCommentRepository";
    @Inject
    public LikeCommentRepository() {}
    public static class BadRequestException extends Exception{
        public BadRequestException(String message) { super(message);}
    }
    public void toggleLikeComment(LikeComment likeComment) throws IOException, JSONException, BadRequestException {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        String likeCommentString = mapper.writeValueAsString(likeComment);

        HttpUrl.Builder urlBuilder = Util.newBuilder()
            .addPathSegment("like-comments");

        Request toggleReq = new Request.Builder()
            .header("Authorization", "Bearer " + KhumuApplication.getToken())
            .put(RequestBody.create(MediaType.parse("application/json"), likeCommentString))
            .url(urlBuilder.build())
            .build();
        Response toggleResp = client.newCall(toggleReq).execute();

        //TODO toggle 하면 log에 message가 전달되는건가?
        if(toggleResp.code() != 201 && toggleResp.code() != 204 && toggleResp.code() != 200) {
            String respString = toggleResp.body().string();
            Log.d(TAG, "toggleLikeComment: " + respString);
            JSONObject respObj = new JSONObject(respString);
            throw new BadRequestException(respObj.getString("message"));
        }

    }
}
