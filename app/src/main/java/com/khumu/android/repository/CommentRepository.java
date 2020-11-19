package com.khumu.android.repository;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.android.data.Article;
import com.khumu.android.data.Comment;
import com.khumu.android.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommentRepository {
    public CommentRepository(){}
    public ArrayList<Comment> ListComment() throws IOException, JSONException {
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
                .url(Util.APIRootEndpoint + "comment?article=1")
                .build();
        Response fetchResp = client.newCall(req).execute();
//        System.out.println(fetchResp);
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
}
