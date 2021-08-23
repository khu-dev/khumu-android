/**
 * FeedFragment가 사용하는 ViewModel
 * FeedFragment를 상속하거나 포함하면 이 ViewModel을 이용해야할 것으로 예상. *
 */
package com.khumu.android.feed;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khumu.android.data.Announcement;
import com.khumu.android.data.Article;
import com.khumu.android.data.Board;
import com.khumu.android.data.rest.ArticleListResponse;
import com.khumu.android.data.rest.BoardListResponse;
import com.khumu.android.repository.ArticleService;
import com.khumu.android.repository.BoardService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dagger.Module;
import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
@Setter
@Module
public class FeedViewModel extends ViewModel {
    private final static String TAG = "FeedViewModel";
    public String debuggingMessage = "debug";
    private BoardService boardService;
    private ArticleService articleService;
    private String nextCursor = "";
    private MutableLiveData<List<Announcement>> announcements;
    private MutableLiveData<List<Board>> boards;
    private MutableLiveData<List<Article>> articles;
    private MutableLiveData<Board> currentBoard;
    /**
     * 하나의 Board에 대한 feed를 이용하고자 하는 경우 => board가 null이 아니라 유효한 값을 가져야한다.
     * Board를 주입해줘야하며 현재는 내가 쓴 게시물, 내가 좋아요한 게시물 등의 논리적 게시판을 볼 수 있게 해준다.
     *
     * 후에는 특정 게시판에 대한 피드도 제공해야하고, Board뿐만 아니라 특정 Tag에 대한 피드 기능도 추가되어야할 것이다.
    */
    public FeedViewModel(ArticleService articleService, Board board){
        Log.d(TAG, "FeedViewModel: (ArticleRepository articleRepository, Board board)");
        this.articleService = articleService;
        this.currentBoard = new MutableLiveData<>(board);
        articles = new MutableLiveData<>(new ArrayList<Article>());
    }

    /**
     * 여러 Board에 대한 feed를 이용하는 경우 board를 null로 둔다. TabFeedFragment에서 Board List를 하기에 사용된다.
     */
    public FeedViewModel(BoardService boardService, ArticleService articleService) {
        Log.d(TAG, "FeedViewModel: (BoardRepository boardRepository, ArticleRepository articleRepository)");
        this.boardService = boardService;
        this.articleService = articleService;

        boards = new MutableLiveData<>(new ArrayList<Board>());
        currentBoard = new MutableLiveData<>(null);
        articles = new MutableLiveData<>(new ArrayList<Article>());
        announcements = new MutableLiveData<>(new ArrayList<>());
        listRecentAnnouncements();
    }

    public void clearArticles(){
        this.articles.getValue().clear();
    }

    // 특정 카테고리 혹은 follow 중인 board를 조회할 수 있음.
    public void listBoards(String category, Boolean followed) {
        Log.d(TAG, "ListBoards: ");
        Call<BoardListResponse> call = boardService.getFollowingBoards("application/json", true);
        call.enqueue(new Callback<BoardListResponse>() {
            @Override
            public void onResponse(Call<BoardListResponse> call, Response<BoardListResponse> response) {
                if (response.isSuccessful()) {
                    boards.postValue(response.body().getData());
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

    // DB 상의 모든 Boards를 list 하여 저장
    public void listBoards() {
        listBoards(null, null);
    }

    // Articles를 list하여 저장
    public void listArticles(){
        String boardName = "following";

        // following이란느 논리적 게시판이 아니라 특정 게시판을 지칭.
        if (currentBoard.getValue() != null) {
            boardName = currentBoard.getValue().getName();
        }

        Call<ArticleListResponse> call = articleService.getArticles(boardName, 10);

        call.enqueue(new Callback<ArticleListResponse>() {
            @Override
            public void onResponse(Call<ArticleListResponse> call, Response<ArticleListResponse> response) {
                if (response.isSuccessful()) {
                    articles.postValue(response.body().getData());
                    nextCursor = getNextCursor(response.body().getLinks().getNext());
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArticleListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    
    public void loadMoreArticles() {
        String boardName = "following";
        if (nextCursor.equals("")) return;
        if (currentBoard.getValue() != null) {
            boardName = currentBoard.getValue().getName();
        }

        Call<ArticleListResponse> call = articleService.getNextCursorArticles(boardName, nextCursor, 10);

        call.enqueue(new Callback<ArticleListResponse>() {
            @Override
            public void onResponse(Call<ArticleListResponse> call, Response<ArticleListResponse> response) {
                if (response.isSuccessful()) {
                    List<Article> tempList = new ArrayList<>();
                    tempList.addAll(response.body().getData());
                    articles.postValue(tempList);
                    nextCursor = getNextCursor(response.body().getLinks().getNext());
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArticleListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    
    public void listRecentAnnouncements() {
        for (int i = 0; i < 3; i++) {
            announcements.getValue().add(Announcement.builder()
                    .author(Announcement.AnnouncementAuthor.builder().name("더미작성자").followed(true).build())
                    .title("더미 공지사항입니다. " + i)
                    .referenceUrl("https://github.com/umi0410").build());
        }
        announcements.postValue(announcements.getValue());
    }

    public String getNextCursor(String next) {
        if (next == null) return "";
        final String REGET_GET_NEXT_CURSOR = "(\\bcursor=\\b)(.*?)(\\b&size\\b)";
        Pattern pattern = Pattern.compile(REGET_GET_NEXT_CURSOR);
        Matcher matcher = pattern.matcher(next);
        if (matcher.find()) {
            return matcher.group(2).trim();
        }
        return "";
    }
}