package com.khumu.android.feed;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dagger.Module;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.android.data.Board;
import com.khumu.android.data.RecentBoard;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.BoardRepository;
import com.khumu.android.util.Util;
import com.khumu.android.data.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

@Module
public class FeedViewModel extends ViewModel {
    private final static String TAG = "FeedViewModel";
    private BoardRepository boardRepository;
    private ArticleRepository articleRepository;

    private MutableLiveData<List<Board>> boards;
    private MutableLiveData<List<Article>> articles;
    private MutableLiveData<Board> currentBoard;

    @Inject
    public FeedViewModel(BoardRepository boardRepository, ArticleRepository articleRepository) {
        articles = new MutableLiveData<>(new ArrayList<Article>());
        List<Board> initialBoards = new ArrayList<Board>();
        Board initialBoard = new RecentBoard();
        initialBoards.add(initialBoard);
        boards = new MutableLiveData<>(initialBoards);
        currentBoard = new MutableLiveData<>(initialBoard);

        this.boardRepository = boardRepository;
        this.articleRepository = articleRepository;
        ListBoards();
        // 기본적으로 recent 게시물들을 1 page 가져옴.
        // 초기 게시판을 recent로 한 경우와 임의의 게시판을 설정한 경우로 나뉨.
//        if (initialBoard instanceof RecentBoard){
//            ListArticles(null, 1);
//        }else{
//            ListArticles(initialBoard.getName(), 1);
//        }
        ListArticles();
    }

    public void clearArticles(){
        this.articles.getValue().clear();
    }

    public MutableLiveData<List<Article>> getLiveDataArticles(){
        return articles;
    }

    public MutableLiveData<List<Board>> getLiveDataBoards(){
        return boards;
    }

    public MutableLiveData<Board> getLiveDataCurrentBoard(){
        return currentBoard;
    }

    public Board getCurrentBoard() {
        return currentBoard.getValue();
    }

    public void setCurrentBoard(Board board) {
        // post로 하면 setCurrentBoard 후에 List 할 때 currentBoard가 최신화가 안되어있을 수 있음.
        currentBoard.setValue(board);
    }

    public void ListBoards() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    boards.postValue(boardRepository.ListBoards());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    // Articles를 초기화
    public void ListArticles(){
        String tempBoard = currentBoard.getValue().getName();
        if(tempBoard == "recent"){
            tempBoard = null; // null로 하면 board 상관없이 가져온다.
        }
        final String board = tempBoard;

        int page = 1;

        new Thread(){
            @Override
            public void run() {
                try {
                    List<Article> _articles = articleRepository.ListArticle(board, page);
                    articles.postValue(_articles);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
        }.start();
    }

    // 기존에 존재하던 Articles에 추가. 미완성.
    public void ListExtraArticles(String board, int page){
        new Thread(){
            @Override
            public void run() {
                try {
                    List<Article> originalArticles = articles.getValue();
                    for (Article newArticle: articleRepository.ListArticle(board, page)){
                        // 기존에 없던 새로운 article인지 확인
                        List<Article> duplicatedArticles = originalArticles.stream().filter(item->{
                            return (newArticle.getID()==(item.getID()));
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

}