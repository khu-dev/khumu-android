package com.khumu.android.repository;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Article;
import com.khumu.android.data.Board;
import com.khumu.android.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class BoardRepository {
    @Inject
    public BoardRepository(){}
    public List<Board> ListBoards() throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = Util.newBuilder()
            .addPathSegment("boards");

        Request.Builder req = new Request.Builder();
        if(KhumuApplication.getToken() != null){
            req.header("Authorization", "Bearer "+ KhumuApplication.getToken());
        }
        req = req.url(urlBuilder.build());

        Response fetchResp = client.newCall(req.build()).execute();
        String respString = fetchResp.body().string();
        // String으로 받아온 것중 articles에 해당하는 "data" 값만 가져온다
        String data = new JSONObject(respString).getString("data");
        JSONArray articleJSONArray = new JSONArray(data);
        ArrayList<Article> articles = new ArrayList<>();

        ObjectMapper mapper  = new ObjectMapper();
        // 이걸 해야 정의하지 않은 property가 있어도 에러가 안남.
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<Board> boards = mapper.readValue(articleJSONArray.toString(), new TypeReference<List<Board>>(){});

        return boards;
    }
}
