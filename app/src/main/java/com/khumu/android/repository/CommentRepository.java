package com.khumu.android.repository;

import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Comment;
import com.khumu.android.data.SimpleComment;
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
public class CommentRepository {
    private final static String TAG = "CommentRepository";
    @Inject
    public CommentRepository(){}
    public ArrayList<Comment> ListComment(String articleID) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = Util.newBuilder()
                .addPathSegment("comments")
                .addQueryParameter("article", articleID)
                .addQueryParameter("parent", "0");

        Request req = new Request.Builder()
                .header("Authorization", "Bearer "+ KhumuApplication.getToken())
                .url(urlBuilder.build())
                .build();
        Response fetchResp = client.newCall(req).execute();
        String respString = fetchResp.body().string();
        String data = new JSONObject(respString).getString("data");
        JSONArray commentJSONArray = new JSONArray(data);
        ArrayList<Comment> comments = new ArrayList<>();

        ObjectMapper mapper  = new ObjectMapper();
        // 이걸 해야 정의하지 않은 property가 있어도 에러가 안남.
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        for(int i=0; i < commentJSONArray.length(); i++){
            Comment comment = mapper.readValue(commentJSONArray.getJSONObject(i).toString(), Comment.class);
            comments.add(comment);
        }
        return comments;
    }

    public ArrayList<Comment> ListReply(String articleID, String commentID) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = Util.newBuilder()
                .addPathSegment("comments")
                .addQueryParameter("article", articleID)
                .addQueryParameter("parent", commentID);

        Request req = new Request.Builder()
                .header("Authorization", "Bearer "+ KhumuApplication.getToken())
                .url(urlBuilder.build())
                .build();
        Response fetchResp = client.newCall(req).execute();
        String respString = fetchResp.body().string();
        String data = new JSONObject(respString).getString("data");
        JSONArray commentJSONArray = new JSONArray(data);
        ArrayList<Comment> comments = new ArrayList<>();

        ObjectMapper mapper  = new ObjectMapper();
        // 이걸 해야 정의하지 않은 property가 있어도 에러가 안남.
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        for(int i=0; i < commentJSONArray.length(); i++){
            Comment comment = mapper.readValue(commentJSONArray.getJSONObject(i).toString(), Comment.class);
            comments.add(comment);
        }
        return comments;
    }

    public boolean CreateComment(SimpleComment simpleComment, String articleID) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        String commentStr = mapper.writeValueAsString(simpleComment);
        System.out.println(commentStr);

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), commentStr);
        System.out.println(body);
        HttpUrl.Builder urlBuilder = Util.newBuilder()
                .addPathSegment("comments");

        Request req = new Request.Builder()
                .header("Authorization", "Bearer " + KhumuApplication.getToken())
                .post(body)
                .url(urlBuilder.build())
                .build();

        Response resp = client.newCall(req).execute();
        System.out.println(resp);
        // 계속 200이듬
        if(resp.code() == 200){
            return true;
        } else{
            return false;
        }
    }

    public void toggleLikeComment(int commentID) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = Util.newBuilder()
                .addPathSegment("comments")
                .addPathSegment(String.valueOf(commentID))
                .addPathSegment("likes");

        Request toggleReq = new Request.Builder()
                .header("Authorization", "Bearer " + KhumuApplication.getToken())
                .patch(RequestBody.create(MediaType.parse("application/json"), "{}")) // empty body. like를 toggle할 때에는 body 필요없음.
                .url(urlBuilder.build())
                .build();
        Response toggleResp = client.newCall(toggleReq).execute();
        //toggle 하면 log에 message가 전달되는건가? -> 된다
        if(toggleResp.code() != 201 && toggleResp.code() != 204 && toggleResp.code() != 200) {
            String respString = toggleResp.body().string();
            Log.d(TAG, "toggleLikeComment: " + respString);
            Log.d(TAG, "[ERROR] toggleLikeComment: wrong response");
            JSONObject respObj = new JSONObject(respString);
        }
    }
}
