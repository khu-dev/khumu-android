package com.khumu.android.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khumu.android.data.Article.Article;
import com.khumu.android.data.Board;
import com.khumu.android.repository.BoardRepository;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private BoardRepository br;
    private MutableLiveData<List<Article>> recentArticles;

    public HomeViewModel(BoardRepository br){
        this.br = br;
        recentArticles = new MutableLiveData<>();
        this.ListRecentBoards();
    }
    public MutableLiveData<List<Article>> getLiveDataRecentArticles(){
        return recentArticles;
    }
    public void ListRecentBoards() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    List<Board> boards = br.ListBoards(null, true);

//                    for(int i=0; i<boards.size(); i++){
//                        Board board = boards.get(i);
                    List<Article> articles = new ArrayList<>();
                    for(Board board: boards){
                        if (board.getRecentArticles().size() != 0){
                            articles.addAll(board.getRecentArticles());
                        }
                    }
                    recentArticles.postValue(articles);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}