package com.khumu.android.articleDetail;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khumu.android.data.Article;
import com.khumu.android.data.Comment;
import com.khumu.android.data.SimpleComment;
import com.khumu.android.data.rest.CommentListResponse;
import com.khumu.android.repository.ArticleService;
import com.khumu.android.repository.CommentService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CommentViewModel extends ViewModel {

    private final static String TAG = "CommentViewModel";
    public CommentService commentService;
    public ArticleService articleService;
    private MutableLiveData<ArrayList<Comment>> comments;
    private MutableLiveData<Article> article;
    //article은 변하는 값을 observe할 데이터가 아니라 MutableLiveData로 하지 않아도 된다.
    private String articleID;
    private Context context;
    public CommentViewModel(Context context, ArticleService articleService, CommentService commentService, String articleID) {
        this.context = context;
        this.articleService = articleService;
        this.commentService = commentService;
        comments = new MutableLiveData<>();
        comments.setValue(new ArrayList<Comment>());
        article = new MutableLiveData<>();
        article.setValue(new Article());
        this.articleID = articleID;
        ListComment();
    }

    public MutableLiveData<Article> getLiveDataArticle() {
        return article;
    }

    public MutableLiveData<ArrayList<Comment>> getLiveDataComments(){
        return comments;
    }

    public void getArticle() {
        Call<Article> call = articleService.getArticle(Integer.valueOf(articleID));
        call.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                Article tempArticle = response.body();
                article.postValue(tempArticle);
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void ListComment() {
        Call<CommentListResponse> call = commentService.getComments(Integer.valueOf(articleID));
        call.enqueue(new Callback<CommentListResponse>() {
            @Override
            public void onResponse(Call<CommentListResponse> call, Response<CommentListResponse> response) {
                //Log.d(TAG, String.valueOf(response.code()));
                List<Comment> tempList = response.body().getData();
                ArrayList<Comment> commentsList = new ArrayList<>();
                commentsList.addAll(tempList);
                //System.out.println(commentsList);
                comments.postValue(commentsList);

            }

            @Override
            public void onFailure(Call<CommentListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void CreateComment(SimpleComment comment) throws Exception{
        Call<Comment> call = commentService.createComment("application/json", comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                Log.d(TAG, "onResponse: " + response.code());
                Toast.makeText(context, "댓글을 작성했습니다", Toast.LENGTH_LONG).show();
                // 비동기적으로 event를 실행하므로 쓰레드에 텀을 주어 Comment를 생성하기 전에 ListComment()하는 것을 막아준다.
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ListComment();
            }
            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "댓글을 작성하지 못했습니다", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void DeleteComment(int commentId) {
        Call<Comment> call = commentService.deleteComment("application/json", Integer.valueOf(commentId));
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                Log.d(TAG, "onResponse: " + response.code());
                Toast.makeText(context, "댓글을 삭제했습니다", Toast.LENGTH_LONG).show();
                ListComment();
            }
            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "알 수 없는 이유로 삭제하지 못했습니다", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void LikeComment(int commentId) throws Exception{
        Call<Comment> call = commentService.likeComment("application/json", commentId);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                Log.d(TAG, "onResponse: " + response.code());
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "알 수 없는 이유로 좋아요 처리를 못했습니다", Toast.LENGTH_LONG).show();
            }
        });
    }
}