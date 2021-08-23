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
    public MutableLiveData<Boolean> isLectureBoard;
    private String currentCategory;

    public BoardViewModel(Context context, BoardService boardService) {
        this.context = context;
        this.boardService = boardService;
        followingBoards = new MutableLiveData<>();
        categoryBoards = new MutableLiveData<>();
        followingBoards.setValue(new ArrayList<Board>());
        categoryBoards.setValue(new ArrayList<Board>());
        isLectureBoard = new MutableLiveData<>();
        isLectureBoard.setValue(false);
        listFollowingBoards();
        currentCategory = "official";
        listCategoryBoards(currentCategory);
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
        currentCategory = category;
        // 강의 게시판일 경우 아무 것도 불러오지 않고 검색해달라는 텍스트를 띄워줌
        if (category.equals("lecture")) {
            categoryBoards.postValue(new ArrayList<Board>());
            isLectureBoard.postValue(true);
            return;
        }
        isLectureBoard.postValue(false);
        Call<BoardListResponse> call = boardService.getUnFollowingBoardsByCategory("application/json", category,false, 300);
        call.enqueue(new Callback<BoardListResponse>() {
            @Override
            public void onResponse(Call<BoardListResponse> call, Response<BoardListResponse> response) {
                if (response.isSuccessful()) {
                    categoryBoards.postValue(response.body().getData());
                    Log.d(TAG, "Response : " + response.raw().toString());
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

    public void followBoard(Board board) {
        Log.d(TAG, "followBoard: " + board.getName());
        Call<Board> call = boardService.followBoard("application/json", board.getName());
        call.enqueue(new Callback<Board>() {
            @Override
            public void onResponse(Call<Board> call, Response<Board> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, board.getDisplayName() + " 게시판을 팔로우했습니다", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Response : " + response.message());
                    listFollowingBoards();
                    listCategoryBoards(currentCategory);
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

    public void unfollowBoard(Board board) {
        Log.d(TAG, "followBoard: " + board.getName());
        Call<Board> call = boardService.unfollowBoard("application/json", board.getName());
        call.enqueue(new Callback<Board>() {
            @Override
            public void onResponse(Call<Board> call, Response<Board> response) {
                if (response.isSuccessful()) {
                    listFollowingBoards();
                    if (currentCategory.equals(board.getCategory()))
                        listCategoryBoards(currentCategory);
                    Toast.makeText(context, board.getDisplayName() + " 게시판을 언팔로우했습니다", Toast.LENGTH_LONG).show();
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
