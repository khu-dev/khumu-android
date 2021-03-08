package com.khumu.android.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Article;
import com.khumu.android.data.Board;
import com.khumu.android.data.Notification;
import com.khumu.android.data.rest.NotificationListResponse;
import com.khumu.android.repository.BoardRepository;
import com.khumu.android.repository.NotificationService;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
@Setter
public class HomeViewModel extends ViewModel {
    private BoardRepository br;
    private NotificationService ns;
    private MutableLiveData<List<Article>> recentArticles;
    private MutableLiveData<List<Notification>> notifications;
    private final String TAG = "HomeViewModel";

    public HomeViewModel(BoardRepository br, NotificationService ns){
        this.br = br;
        this.ns = ns;
        recentArticles = new MutableLiveData<>();
        notifications = new MutableLiveData<>(new ArrayList<Notification>());
        this.listRecentBoards();
        this.listNotifications();
    }
    public MutableLiveData<List<Article>> getLiveDataRecentArticles(){
        return recentArticles;
    }
    public void listRecentBoards() {
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

    public void listNotifications() {
        Call<NotificationListResponse> call = ns.getNotifications(KhumuApplication.getUsername());
        call.enqueue(new Callback<NotificationListResponse>() {
            @Override
            public void onResponse(Call<NotificationListResponse> call, Response<NotificationListResponse> response) {
                if (response.code() == 200) {
                    notifications.postValue(response.body().getData());
                } else{
                    Log.e(TAG, "onResponse: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<NotificationListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}