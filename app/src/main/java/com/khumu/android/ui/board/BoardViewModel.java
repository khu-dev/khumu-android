package com.khumu.android.ui.board;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.khumu.android.Helper;
import com.khumu.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BoardViewModel extends ViewModel {
    OkHttpClient client = new OkHttpClient();
    private MutableLiveData<ArrayList<ArticleData>> articleDataArrayList;
    public BoardViewModel() {
        articleDataArrayList = new MutableLiveData<>();
        articleDataArrayList.setValue(new ArrayList<ArticleData>());
        try{
            System.out.println(1);
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
            System.out.println(2);
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public MutableLiveData<ArrayList<ArticleData>> getLiveDataArticles(){
        return articleDataArrayList;
    }

    public String FetchArticles() throws IOException, JSONException {
        RequestBody authBody = RequestBody.create(MediaType.parse("application/json"),
                "{\"username\":\"admin\",\"password\":\"123123\"}");
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
        Response resp = client.newCall(req).execute();
        if (resp.isSuccessful())
        System.out.println(resp.header("status"));
        String respString = resp.body().string();
        JSONArray respObj = new JSONArray(respString);
        for (int i=0; i<respObj.length(); i++){
            JSONObject articleObj = respObj.getJSONObject(i);
            ArrayList<ArticleData> arr = articleDataArrayList.getValue();

                arr.add(new ArticleData(
                        articleObj.getString("title"),
                        articleObj.getString("content")
                ));
            System.out.println("size" + arr.size());
            articleDataArrayList.postValue(arr);
        }
        System.out.println(respObj);
        System.out.println(respString);
        return respString;
    }
}