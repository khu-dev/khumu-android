package com.khumu.android.feed;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.util.Util;
import com.khumu.android.data.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FeedViewModel extends ViewModel {
    private ArticleRepository articleRepository;
    private MutableLiveData<ArrayList<Article>> articles;

    public FeedViewModel(ArticleRepository articleRepository) {
        articles = new MutableLiveData<>();
        articles.setValue(new ArrayList<Article>());
        this.articleRepository = articleRepository;
        ListArticle();
    }

    public MutableLiveData<ArrayList<Article>> getLiveDataArticles(){
        return articles;
    }

    public void ListArticle(){
        new Thread(){
            @Override
            public void run() {
                try {
                    ArrayList<Article> originalArticles = articles.getValue();
                    for (Article newArticle: articleRepository.ListArticle()){
                        // 기존에 없던 새로운 article인지 확인
                        List<Article> duplicatedArticles = originalArticles.stream().filter(item->{
                            return (newArticle.getID().equals(item.getID()));
                        }).collect(Collectors.toList());
                        if(duplicatedArticles.size() == 0){
                            originalArticles.add(newArticle);
                        }
                        else{
                        }
                    }
                    articles.postValue(originalArticles);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void CreateArticle(Article article) throws Exception{
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        String articleString = mapper.writeValueAsString(article);
        JSONObject articleJSON = new JSONObject(articleString);
        //System.out.println(articleString);
        RequestBody authBody = RequestBody.create(MediaType.parse("application/json"),
                String.format("{\"username\":\"%s\",\"password\":\"%s\"}", Util.DEFAULT_USERNAME, Util.DEFAULT_PASSWORD)
        );
        Request authReq = new Request.Builder()
                .post(authBody)
                .url(Util.APIRootEndpoint + "token")
                .build();
        Response authResp = client.newCall(authReq).execute();
        String authRespStr = authResp.body().string();
        String token = new JSONObject(authRespStr).getString("access");

        Request createReq = new Request.Builder()
                .header("Authorization", "Bearer "+token)
                .post(RequestBody.create(MediaType.parse("application/json"), articleString))
                .url(Util.APIRootEndpoint + "articles")
                .build();
        Response createResp = client.newCall(createReq).execute();
        String createRespStr = createResp.body().string();
        System.out.println("createRespStr: " + createRespStr);
    }

}