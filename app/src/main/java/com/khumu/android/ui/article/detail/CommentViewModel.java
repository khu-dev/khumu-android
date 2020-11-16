package com.khumu.android.ui.article.detail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.android.Helper;
import com.khumu.android.R;
import com.khumu.android.ui.article.detail.CommentData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CommentViewModel extends ViewModel {

    OkHttpClient client = new OkHttpClient();
    private MutableLiveData<ArrayList<CommentData>> commentsData;
    private MutableLiveData<String> articlesID;
    public CommentViewModel() {
        commentsData = new MutableLiveData<>();
        commentsData.setValue(new ArrayList<CommentData>());
        try{
            AsyncTask<String, Void, Response> as = new AsyncTask<String, Void, Response>(){
                @Override
                protected Response doInBackground(String... strings) {
                    try{
                        FetchComments();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            as.execute();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public MutableLiveData<ArrayList<CommentData>> getLiveDataComments(){
        return commentsData;
    }

    public String FetchComments() throws IOException, JSONException {

        RequestBody authBody = RequestBody.create(MediaType.parse("application/json"),
                String.format("{\"username\":\"%s\",\"password\":\"%s\"}", Helper.DEFAULT_USERNAME, Helper.DEFAULT_PASSWORD)
        );
        Request authReq = new Request.Builder()
                .post(authBody)
                .url(Helper.APIRootEndpoint + "token/")
                .build();
        Response authResp = client.newCall(authReq).execute();
        String authRespStr = authResp.body().string();
        String token = new JSONObject(authRespStr).getString("access");
        System.out.println(token);
        //TODO Article id를 받아와야하는데 아직 해결못함
        Request req = new Request.Builder()
                .header("Authorization", "Bearer "+token)
                .url(Helper.APIRootEndpoint + "comments/?article=1")
                .build();
        Response fetchResp = client.newCall(req).execute();
//        if (resp.isSuccessful()) System.out.println(resp.header("status"));
        String respString = fetchResp.body().string();
        // String으로 받아온 것중 articles에 해당하는 "data" 값만 가져온다
        String data = new JSONObject(respString).getString("data");
        JSONArray respArray = new JSONArray(data);
        ArrayList<CommentData> originalComments = commentsData.getValue();
        for (int i=0; i<respArray.length(); i++){
            JSONObject commentObj = respArray.getJSONObject(i);
            System.out.println(commentObj);
            List<CommentData> duplicatedComments = originalComments.stream().filter(item->{
                try {
                    return (commentObj.getString("id").equals(item.getID()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }).collect(Collectors.toList());
            if(duplicatedComments.size() == 0){
//                String authorString = articleObj.isNull("author")?"":articleObj.getString("author");
                String authorString = commentObj.isNull("author")?"":commentObj.getJSONObject("author").getString("username");
                CommentData commentData = new CommentData(
                        commentObj.getString("id"),
                        authorString,
                        commentObj.getString("article"),
                        commentObj.getString("content")
                );
                originalComments.add(commentData);
            }
        }
        commentsData.postValue(originalComments);
        return respString;
    }

    public void CreateComment(CommentData commentData) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        String commentString = mapper.writeValueAsString(commentData);
        JSONObject commentJSON = new JSONObject(commentString);
        System.out.println(commentString);
        RequestBody authBody = RequestBody.create(MediaType.parse("application/json"),
                String.format("{\"username\":\"%s\",\"password\":\"%s\"}", Helper.DEFAULT_USERNAME, Helper.DEFAULT_PASSWORD)
        );
        Request authReq = new Request.Builder()
                .post(authBody)
                .url(Helper.APIRootEndpoint + "token/")
                .build();
        Response authResp = client.newCall(authReq).execute();
        String authRespStr = authResp.body().string();
        String token = new JSONObject(authRespStr).getString("access");

        Request createReq = new Request.Builder()
                .header("Authorization", "Bearer "+token)
                .post(RequestBody.create(MediaType.parse("application/json"), commentString))
                // 임시
                .url(Helper.APIRootEndpoint + "comments/" + "?articles=1")
                .build();
        Response createResp = client.newCall(createReq).execute();
        String createRespStr = createResp.body().string();
        System.out.println("createRespStr: " + createRespStr);
    }
}