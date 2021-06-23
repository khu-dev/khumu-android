package com.khumu.android.repository;

import android.util.Log;

import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Board;
import com.khumu.android.data.rest.BoardListResponse;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

public class BoardRepository {

    private final static String TAG="BoardRepository";
    @Inject
    public BoardService service;

    @Inject
    public BoardRepository(){
        KhumuApplication.applicationComponent.inject(this);
//        this.client = RetrofitClient.getClient(Util.APIRootEndpoint);
//        this.service = client.create(BoardService.class);
    }
    public List<Board> ListBoards(String category, Boolean followed) throws IOException, JSONException {

        BoardListResponse respString = new BoardListResponse();
        if (category!=null && !category.equals("")){
            Call<BoardListResponse> call = service.getBoardsByCategory("application/json", category);
            respString = call.execute().body();
        }
        if (followed!=null){
            Call<BoardListResponse> call = service.getFollowingBoards("application/json", followed);
            respString = call.execute().body();
        }
        List<Board> boards = new ArrayList<>();
        if (respString != null) {
            List<Board> BoardList = respString.getData();
            Log.d(TAG, "BoardList: " + BoardList.toString());

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

        }

        return boards;
    }
}
