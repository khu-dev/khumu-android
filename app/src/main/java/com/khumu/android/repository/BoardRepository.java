package com.khumu.android.repository;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Board;
import com.khumu.android.data.BoardListResponse;
import com.khumu.android.retrofitInterface.BoardService;
import com.khumu.android.util.RetrofitClient;
import com.khumu.android.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.Module;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;

@Module
public class BoardRepository {

    private final static String TAG="BoardRepository";
    @Inject
    public BoardService service;

    @Inject
    public BoardRepository(){
        KhumuApplication.container.inject(this);
//        this.client = RetrofitClient.getClient(Util.APIRootEndpoint);
//        this.service = client.create(BoardService.class);
    }
    public List<Board> ListBoards(String category, Boolean followed) throws IOException, JSONException {

        BoardListResponse respString = new BoardListResponse();
        if (category!=null && !category.equals("")){
            Call<BoardListResponse> call = service.getBoardsCategory("Bearer " + KhumuApplication.getToken(), category);
            respString = call.execute().body();
        }
        if (followed!=null){
            Call<BoardListResponse> call = service.getBoardsFollowed("Bearer " + KhumuApplication.getToken(), String.valueOf(followed));
            respString = call.execute().body();
        }

        List<Board> BoardList = respString.getData();
        Log.d(TAG, "BoardList: " + BoardList.toString());

//        Request.Builder reqBuilder = new Request.Builder();
//        if(KhumuApplication.getToken() != null){
//            reqBuilder.header("Authorization", "Bearer "+ KhumuApplication.getToken());
//        }
//        reqBuilder.url(urlBuilder.build());
//
//        Response fetchResp = client.newCall(reqBuilder.build()).execute();
//        String respString = fetchResp.body().string();
//        // String으로 받아온 것중 articles에 해당하는 "data" 값만 가져온다
//        String data = new JSONObject(respString).getString("data");
//        JSONArray articleJSONArray = new JSONArray(data);
//
//        ObjectMapper mapper  = new ObjectMapper();
//        // 이걸 해야 정의하지 않은 property가 있어도 에러가 안남.
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        List<Board> tmp = mapper.readValue(data, new TypeReference<List<Board>>(){});
        List<Board> boards = new ArrayList<>();
        //logical board부터 담음
        for (Board b: BoardList){
            if (b.getCategory().equals("logical")){
                boards.add(b);
            }
        }

        for (Board b: BoardList){
            if (!b.getCategory().equals("logical")){
                boards.add(b);
            }
        }

        return boards;
    }
}
