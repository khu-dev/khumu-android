package com.khumu.android.search;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khumu.android.data.Article;
import com.khumu.android.data.Board;
import com.khumu.android.data.rest.ArticleListResponse;
import com.khumu.android.data.rest.BoardListResponse;
import com.khumu.android.repository.ArticleService;
import com.khumu.android.repository.BoardService;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
@Setter
public class CommunitySearchViewModel extends ViewModel {
    private final static String TAG = "CommunitySearchViewModel";
    Context context;
    BoardService boardService;
    ArticleService articleService;
    // 검색어
    MutableLiveData<String> searchText;
    MutableLiveData<List<Board>> resultBoards;
    MutableLiveData<List<Article>> resultArticles;


    public CommunitySearchViewModel(Context context, BoardService boardService, ArticleService articleService, String searchText, List<Board> resultBoards, List<Article> resultArticles) {
        this.context = context;
        this.boardService = boardService;
        this.articleService = articleService;
        this.searchText = new MutableLiveData<>(searchText);
        this.resultBoards = new MutableLiveData<>(resultBoards);
        this.resultArticles = new MutableLiveData<>(resultArticles);
    }

    public void search(String search) {
        if (search.isEmpty()) {
            Log.d(TAG, "search: 검색어가 비어있어 더 이상 검색하지 않습니다.");
            this.resultBoards.postValue(new ArrayList<>());
            this.resultArticles.postValue(new ArrayList<>());
            return;
        }
        Log.d(TAG, "search: " + search);
        Log.d(TAG, "search: 게시판 검색 수행");
        boardService.searchBoards(search).enqueue(new Callback<BoardListResponse>() {
            @Override
            public void onResponse(Call<BoardListResponse> call, Response<BoardListResponse> response) {
                if (response.isSuccessful()) {
                    List<Board> boards = response.body().getData();
                    resultBoards.postValue(boards);
                } else{
                    Log.e(TAG, "searchBoards: 게시판 검색 도중 오류 발생 " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<BoardListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        Log.d(TAG, "search: 게시글 검색 수행");
        articleService.searchArticles(search, null).enqueue(new Callback<ArticleListResponse>() {
            @Override
            public void onResponse(Call<ArticleListResponse> call, Response<ArticleListResponse> resp) {
                if (resp.isSuccessful()) {
                    List<Article> articles = resp.body().getData();
                    resultArticles.postValue(articles);
                } else{
                    Log.e(TAG, "searchArticle: 게시글 검색 도중 오류 발생 " + resp.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArticleListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
