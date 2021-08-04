package com.khumu.android.boardList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.tabs.TabLayout;
import com.khumu.android.data.Article;
import com.khumu.android.data.Board;
import com.khumu.android.data.rest.BoardListResponse;
import com.khumu.android.repository.BoardService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardViewModel extends ViewModel {

    private final static String TAG = "BoardListViewModel";
    public BoardService boardService;
    public MutableLiveData<List<Board>> followingBoards;
    public MutableLiveData<List<Board>> categoryBoards;
    private Context context;

    public BoardViewModel(Context context, BoardService boardService) {
        this.context = context;
        this.boardService = boardService;
        followingBoards = new MutableLiveData<>();
        categoryBoards = new MutableLiveData<>();
        followingBoards.setValue(new ArrayList<Board>());
        categoryBoards.setValue(new ArrayList<Board>());
        listFollowingBoards();
        listCategoryBoards("official");
        Log.d(TAG, "Created");
    }

    public void listFollowingBoards() {
        Log.d(TAG, "listFollowingBoards");
        Call<BoardListResponse> call = boardService.getFollowingBoards("application/json", true);
        call.enqueue(new Callback<BoardListResponse>() {
            @Override
            public void onResponse(Call<BoardListResponse> call, Response<BoardListResponse> response) {
                if (response.isSuccessful()) {
                    followingBoards.postValue(response.body().getData());
                    Log.d(TAG, "Response : " + response.raw().toString());
                    Log.d(TAG, "followedBoards : " + response.body().getData());
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

    public void listCategoryBoards(String category) {
        Log.d(TAG, "listCategoryBoards");
        Call<BoardListResponse> call = boardService.getUnFollowingBoardsByCategory("application/json", category,false);
        call.enqueue(new Callback<BoardListResponse>() {
            @Override
            public void onResponse(Call<BoardListResponse> call, Response<BoardListResponse> response) {
                if (response.isSuccessful()) {
                    categoryBoards.postValue(response.body().getData());
                    Log.d(TAG, "Response : " + response.raw().toString());
                    Log.d(TAG, "CategoryBoards : " + response.body().getData());
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

    public void followBoard(String name) {
        Log.d(TAG, "followBoard: " + name);
        Call<Board> call = boardService.followBoard("application/json", name);
        call.enqueue(new Callback<Board>() {
            @Override
            public void onResponse(Call<Board> call, Response<Board> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, name + " 게시판을 팔로우했습니다", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Response : " + response.message());
                    listFollowingBoards();
                    listCategoryBoards("department");
                } else {
                    Toast.makeText(context, "예상치 못한 오류로 팔로우를 하지 못했습니다", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "onResponse: " + response.errorBody().toString());
                }
            }
            @Override
            public void onFailure(Call<Board> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void unfollowBoard(String name) {
        Log.d(TAG, "followBoard: " + name);
        Call<Board> call = boardService.unfollowBoard("application/json", name);
        call.enqueue(new Callback<Board>() {
            @Override
            public void onResponse(Call<Board> call, Response<Board> response) {
                if (response.isSuccessful()) {
                    listFollowingBoards();
                    listCategoryBoards("department");
                    Toast.makeText(context, name + " 게시판을 언팔로우했습니다", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Response : " + response.raw().toString());
                } else {
                    Toast.makeText(context, "예상치 못한 오류로 언팔로우를 하지 못했습니다", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<Board> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
