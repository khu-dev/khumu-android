package com.khumu.android.repository;

import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Article;
import com.khumu.android.data.Comment;
import com.khumu.android.data.SimpleComment;
import com.khumu.android.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.SyncFailedException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.Module;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
@Module
public class CommentRepository {
    private final static String TAG = "CommentRepository";
    @Inject
    public CommentRepository(){}
    public ArrayList<Comment> ListComment(String articleID) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = Util.newBuilder()
            .addPathSegment("comments")
            .addQueryParameter("article", articleID);

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
}
