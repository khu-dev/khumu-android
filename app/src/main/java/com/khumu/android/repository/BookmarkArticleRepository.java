package com.khumu.android.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.android.KhumuApplication;
import com.khumu.android.data.BookmarkArticle;
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
public class BookmarkArticleRepository {
    private final static String TAG = "BookmarkArticleRepository";
    @Inject
    public BookmarkArticleRepository(){}
    public static class BadRequestException extends Exception{
        public BadRequestException(String message){
            super(message);
        }
    }
    public void toggleBookmarkArticle(BookmarkArticle bookmarkArticle) throws IOException, JSONException, BadRequestException {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        String bookmarkArticleString = mapper.writeValueAsString(bookmarkArticle);

        HttpUrl.Builder urlBuilder = Util.newBuilder()
            .addPathSegment("bookmark-articles");

        Request toggleReq = new Request.Builder()
            .header("Authorization", "Bearer " + KhumuApplication.getToken())
            .put(RequestBody.create(MediaType.parse("application/json"), bookmarkArticleString))
            .url(urlBuilder.build())
            .build();
        Response toggleResp = client.newCall(toggleReq).execute();

        if (toggleResp.code() != 201 && toggleResp.code() != 204 && toggleResp.code() != 200){
            String respString = toggleResp.body().string();
            JSONObject respObj = new JSONObject(respString);
            throw new BadRequestException(respObj.getString("message"));
        }
    }
}
