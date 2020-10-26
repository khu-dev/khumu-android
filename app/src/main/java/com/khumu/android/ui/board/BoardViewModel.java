package com.khumu.android.ui.board;

import android.os.AsyncTask;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BoardViewModel extends ViewModel {
    OkHttpClient client = new OkHttpClient();
    private MutableLiveData<ArrayList<ArticleData>> articlesData;
    public BoardViewModel() {
        articlesData = new MutableLiveData<>();
        articlesData.setValue(new ArrayList<ArticleData>());
        try{
            AsyncTask<String, Void, Response> as = new AsyncTask<String, Void, Response>(){
                @Override
                protected Response doInBackground(String... strings) {
                    try{
                        FetchArticles();
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

    public MutableLiveData<ArrayList<ArticleData>> getLiveDataArticles(){
        return articlesData;
    }

    public String FetchArticles() throws IOException, JSONException {
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

        Request req = new Request.Builder()
            .header("Authorization", "Bearer "+token)
            .url(Helper.APIRootEndpoint + "articles/")
            .build();
        Response fetchResp = client.newCall(req).execute();
//        if (resp.isSuccessful()) System.out.println(resp.header("status"));
        String respString = fetchResp.body().string();
        JSONArray respArray = new JSONArray(respString);
        ArrayList<ArticleData> originalArticles = articlesData.getValue();
        for (int i=0; i<respArray.length(); i++){
            JSONObject articleObj = respArray.getJSONObject(i);

            List<ArticleData> duplicatedArticles = originalArticles.stream().filter(item->{
                try {
                    return (articleObj.getString("pk").equals(item.getPk()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }).collect(Collectors.toList());
            if(duplicatedArticles.size() == 0){
//                String authorString = articleObj.isNull("author")?"":articleObj.getString("author");
                String authorString = articleObj.isNull("author")?"":articleObj.getJSONObject("author").getString("username");
                ArticleData articleData = new ArticleData(
                    articleObj.getString("pk"),
                    authorString,
                    articleObj.getString("title"),
                    articleObj.getString("content"),
                    articleObj.getString("comment_count")
                );
                originalArticles.add(articleData);
            }
        }
        articlesData.postValue(originalArticles);
        return respString;
    }

    public void CreateArticle(ArticleData articleData) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        String articleString = mapper.writeValueAsString(articleData);
        JSONObject articleJSON = new JSONObject(articleString);
        System.out.println(articleString);
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
                .post(RequestBody.create(MediaType.parse("application/json"), articleString))
                .url(Helper.APIRootEndpoint + "articles/")
                .build();
        Response createResp = client.newCall(createReq).execute();
        String createRespStr = createResp.body().string();
        System.out.println("createRespStr: " + createRespStr);
    }
}