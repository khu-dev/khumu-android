package com.khumu.android.feed;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dagger.Module;

import com.khumu.android.data.Board;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.BoardRepository;
import com.khumu.android.data.Article;

import org.json.JSONException;

import javax.inject.Inject;

@Module
public class FeedViewModel extends ViewModel {
    private final static String TAG = "FeedViewModel";
    private BoardRepository boardRepository;
    private ArticleRepository articleRepository;

    private Board recentBoard;
    private MutableLiveData<List<Board>> boards;
    private MutableLiveData<List<Article>> articles;
    private MutableLiveData<Board> currentBoard;

    @Inject
    public FeedViewModel(BoardRepository boardRepository, ArticleRepository articleRepository) {
        this.boardRepository = boardRepository;
        this.articleRepository = articleRepository;

        recentBoard = new Board("recent", "logical","최근게시판","임시로 띄우는 최근 게시물", null, null, null);
        currentBoard = new MutableLiveData<>(recentBoard);
        List<Board> initialBoards = new ArrayList<Board>();
        initialBoards.add(recentBoard);
        boards = new MutableLiveData<>(initialBoards);

        articles = new MutableLiveData<>(new ArrayList<Article>());

        ListBoards();
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
                    List<Board> _boards = new ArrayList<>();
                    _boards.addAll(boardRepository.ListBoards(null, null));
                    boards.postValue(_boards);
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
        final String board = currentBoard.getValue().getName();

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