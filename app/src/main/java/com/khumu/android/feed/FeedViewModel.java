/**
 * FeedFragment가 사용하는 ViewModel
 * FeedFragment를 상속하거나 포함하면 이 ViewModel을 이용해야할 것으로 예상. *
 */
package com.khumu.android.feed;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dagger.Module;

import com.khumu.android.data.article.Article;
import com.khumu.android.data.Board;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.BoardRepository;

import org.json.JSONException;

@Module
public class FeedViewModel extends ViewModel {
    private final static String TAG = "FeedViewModel";
    public String debuggingMessage = "debug";
    private BoardRepository boardRepository;
    private ArticleRepository articleRepository;

    private Board recentMyBoard;
    private MutableLiveData<List<Board>> boards;
    private MutableLiveData<List<Article>> articles;
    private MutableLiveData<Board> currentBoard;
    /**
     * 하나의 Board에 대한 feed를 이용하고자 하는 경우 => board가 null이 아니라 유효한 값을 가져야한다.
     * Board를 주입해줘야하며 현재는 내가 쓴 게시물, 내가 좋아요한 게시물 등의 논리적 게시판을 볼 수 있게 해준다.
     *
     * 후에는 특정 게시판에 대한 피드도 제공해야하고, Board뿐만 아니라 특정 Tag에 대한 피드 기능도 추가되어야할 것이다.
    */
    public FeedViewModel(ArticleRepository articleRepository, Board board){
        Log.d(TAG, "FeedViewModel: (ArticleRepository articleRepository, Board board)");
        this.articleRepository = articleRepository;
        this.currentBoard = new MutableLiveData<>(board);

        articles = new MutableLiveData<>(new ArrayList<Article>());
    }

    /**
     * 여러 Board에 대한 feed를 이용하는 경우 board를 null로 둔다. TabFeedFragment에서 Board List를 하기에 사용된다.
     * (WIP) 내가 Follow한 게시판을 조회하고, 전체 게시물도 조회하도록한다. (? 전에 써놓은 건데 무슨 소리지)
     */
    public FeedViewModel(BoardRepository boardRepository, ArticleRepository articleRepository) {
        Log.d(TAG, "FeedViewModel: (BoardRepository boardRepository, ArticleRepository articleRepository)");
        this.boardRepository = boardRepository;
        this.articleRepository = articleRepository;
        recentMyBoard = new Board("following", null,"Following","내가 팔로우한 게시판들로 이루어진 피드입니다.", false, null, null);
        currentBoard = new MutableLiveData<>(recentMyBoard);
        List<Board> tmpBoards = new ArrayList<Board>();
        tmpBoards.add(recentMyBoard);
        boards = new MutableLiveData<>(tmpBoards);

        articles = new MutableLiveData<>(new ArrayList<Article>());
    }

    public void clearArticles(){
        this.articles.getValue().clear();
    }

    public LiveData<List<Article>> getLiveDataArticles(){
        return articles;
    }

    public LiveData<List<Board>> getLiveDataBoards(){
        return boards;
    }

    public LiveData<Board> getLiveDataCurrentBoard(){
        return currentBoard;
    }

    public Board getCurrentBoard() {
        return currentBoard.getValue();
    }

    public void setCurrentBoard(Board board) {
        // post로 하면 setCurrentBoard 후에 List 할 때 currentBoard가 최신화가 안되어있을 수 있음.
        currentBoard.setValue(board);
    }

    // board가 변경되면 그에 맞게 article도 list
    public void setCurrentBoard(String boardDisplayName) {
        for(Board b: boards.getValue()){
            if(b.getDisplayName().equals(boardDisplayName)){
                currentBoard.setValue(b);
            }
        }

        ListArticles();
    }

    // 특정 카테고리 혹은 follow 중인 board를 조회할 수 있음.
    public void ListBoards(String category, Boolean followed){
        Log.d(TAG, "ListBoards: ");
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    List<Board> _boards = new ArrayList<>();
                    // ListBoards API에서는 최근 내가 팔로우한 피드를 보여주지 않는다.
                    // 따라서 수동으로 추가해준다.
                    _boards.add(recentMyBoard);
                    _boards.addAll(boardRepository.ListBoards(category, followed));
                    boards.postValue(_boards);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    // 모든 Boards를 list 하여 저장
    public void ListBoards() {
        ListBoards(null, null);
    }

    // Articles를 list하여 저장
    public void ListArticles(){
        Log.d(TAG, "ListArticles: " + currentBoard.getValue().getName());
        new Thread(){
            @Override
            public void run() {
                try {
                    List<Article> _articles = articleRepository.ListArticle(currentBoard.getValue().getName(), 1);
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
                            return (newArticle.getId()==(item.getId()));
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