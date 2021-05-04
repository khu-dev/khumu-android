package com.khumu.android.BoardList;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.khumu.android.data.Board;
import com.khumu.android.data.rest.BoardListResponse;
import com.khumu.android.repository.BoardService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardViewModel {

    private final static String TAG = "BoardListViewModel";
    public BoardService boardService;
    private MutableLiveData<List<Board>> followedBoards;
    private MutableLiveData<List<Board>> unFollowedboards;

    public BoardViewModel(Context context, BoardService boardService) {
        this.boardService = boardService;
        followedBoards = new MutableLiveData<>();
        unFollowedboards = new MutableLiveData<>();
        followedBoards.setValue(new ArrayList<Board>());
        unFollowedboards.setValue(new ArrayList<Board>());
    }

    public void listFollowedBoards() {
        Log.d(TAG, "listFollowedBoards: ");
        Call<BoardListResponse> call = boardService.getFollowingBoards(true);
        call.enqueue(new Callback<BoardListResponse>() {
            @Override
            public void onResponse(Call<BoardListResponse> call, Response<BoardListResponse> response) {
                if (response.isSuccessful()) {
                    followedBoards.postValue(response.body().getData());
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<BoardListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void listBoards(String category) {
        Call<BoardListResponse> call = boardService.getUnFollowingBoardsByCategory(category,false);
        call.enqueue(new Callback<BoardListResponse>() {
            @Override
            public void onResponse(Call<BoardListResponse> call, Response<BoardListResponse> response) {
                if (response.isSuccessful()) {
                    followedBoards.postValue(response.body().getData());
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<BoardListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
