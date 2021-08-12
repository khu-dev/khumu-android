package com.khumu.android.articleDetail;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Article;
import com.khumu.android.data.Comment;
import com.khumu.android.data.ResourceSubscription;
import com.khumu.android.data.SimpleComment;
import com.khumu.android.data.rest.ArticleResponse;
import com.khumu.android.data.rest.CommentListResponse;
import com.khumu.android.data.rest.DefaultResponse;
import com.khumu.android.data.rest.ResourceSubscriptionResponse;
import com.khumu.android.repository.ArticleService;
import com.khumu.android.repository.CommentService;
import com.khumu.android.repository.NotificationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// TODO(gusrl4025) - 여기 이름 좀 CommentViewModel 에서 ArticleDetailViewModel 같은 걸로 좀 바꿔주세요.
public class CommentViewModel extends ViewModel {

    private final static String TAG = "CommentViewModel";
    public CommentService commentService;
    public ArticleService articleService;
    public NotificationService notificationService;
    private final MutableLiveData<ArrayList<Comment>> comments;
    private final MutableLiveData<Article> article;
    private final MutableLiveData<Boolean> isArticleSubscribed;
    private final Retrofit retrofit;
    //article은 변하는 값을 observe할 데이터가 아니라 MutableLiveData로 하지 않아도 된다.
    private String articleID;
    private Context context;
    public CommentViewModel(Context context, Retrofit retrofit, ArticleService articleService, CommentService commentService, NotificationService notificationService, String articleID) {
        this.context = context;
        this.retrofit = retrofit;
        this.articleService = articleService;
        this.commentService = commentService;
        this.notificationService = notificationService;
        comments = new MutableLiveData<>(new ArrayList<Comment>());
        article = new MutableLiveData<>(new Article());
        isArticleSubscribed = new MutableLiveData<>();
        this.articleID = articleID;
        fetchArticle();
        getIsArticleSubscribed();
        listComment();

    }

    public MutableLiveData<Article> getLiveDataArticle() {
        return article;
    }

    public MutableLiveData<ArrayList<Comment>> getLiveDataComments(){
        return comments;
    }

    public MutableLiveData<Boolean> getArticleSubscribed() { return isArticleSubscribed; }

    public void fetchArticle() {
        Call<ArticleResponse> call = articleService.getArticle(Integer.valueOf(articleID));
        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Article : " + String.valueOf(response.body().getArticle().getContent()));
                    Log.d(TAG, "Article : " + response.raw().toString());
                    Article tempArticle = response.body().getArticle();
                    article.postValue(tempArticle);
                } else {
                    Log.e(TAG, "onResponse: Article(id=" + articleID + ")를 조회하던 중 오류 발생. " + response.errorBody());
                }

            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void likeArticleToggle() {
        Call<DefaultResponse> call = articleService.likeArticleToggle("application/json", Integer.valueOf(articleID));
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 201) {
                        article.getValue().setLiked(true);
                        article.getValue().setLikeArticleCount(article.getValue().getLikeArticleCount() + 1);
                    } else if (response.code() == 204) {
                        article.getValue().setLiked(false);
                        article.getValue().setLikeArticleCount(article.getValue().getLikeArticleCount() - 1);
                    } else{
                        Toast.makeText(context, "올바르지 않은 응답입니다. 쿠뮤에 문의해주세요.", Toast.LENGTH_SHORT).show();
                    }

                    article.postValue(article.getValue());
                } else {
                    if (response.code() == 400) {
                        try {
                            DefaultResponse errorResponse = (DefaultResponse) retrofit.responseBodyConverter(DefaultResponse.class, DefaultResponse.class.getAnnotations()).convert(response.errorBody());
                            Toast.makeText(context, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else{
                        Log.e(TAG, "onResponse: " + response.errorBody());
                    }

                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(context, "알 수 없는 이유로 좋아요를 수행하지 못했습니다", Toast.LENGTH_SHORT);
                t.printStackTrace();
            }
        });
    }

    public void bookmarkArticleToggle() {
        Call<DefaultResponse> call = articleService.bookmarkArticleToggle("application/json", Integer.valueOf(articleID));
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 201) {
                        article.getValue().setBookmarked(true);
                        article.getValue().setBookmarkArticleCount(article.getValue().getBookmarkArticleCount() + 1);
                    } else if (response.code() == 204) {
                        article.getValue().setBookmarked(false);
                        article.getValue().setBookmarkArticleCount(article.getValue().getBookmarkArticleCount() - 1);
                    } else {
                        Toast.makeText(context, "올바르지 않은 응답입니다. 쿠뮤에 문의해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    article.postValue(article.getValue());
                } else {
                        if (response.code() == 400) {
                            DefaultResponse errorResponse = null;
                            try {
                                errorResponse = (DefaultResponse) retrofit.responseBodyConverter(DefaultResponse.class, DefaultResponse.class.getAnnotations()).convert(response.errorBody());
                                Toast.makeText(context, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else{
                            Log.e(TAG, "onResponse: " + response.errorBody());
                        }

                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(context, "알 수 없는 이유로 북마크를 수행하지 못했습니다", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public void subscribeArticle() {
        ResourceSubscription subscription = new ResourceSubscription("article", Integer.valueOf(articleID));
        Call<Object> call = notificationService.subscribeResource("application/json", subscription);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d(TAG, "Article " + articleID + " Subscribed" );
                Toast.makeText(context, "게시물을 구독했습니다", Toast.LENGTH_LONG);
                isArticleSubscribed.postValue(true);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(context, "알 수 없는 이유로 게시물을 구독하지 못했습니다", Toast.LENGTH_LONG);
                t.printStackTrace();
            }
        });
    }

    public void unsubscribeArticle() {
        ResourceSubscription subscription = new ResourceSubscription("article", Integer.valueOf(articleID));
        Call<Object> call = notificationService.unsubscribeResource("application/json", subscription);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d(TAG, "Article " + articleID + " Unsubscribed" );
                Toast.makeText(context, "게시물을 구독 취소했습니다", Toast.LENGTH_LONG);
                isArticleSubscribed.postValue(false);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(context, "알 수 없는 이유로 게시물을 구독 취소하지 못했습니다", Toast.LENGTH_LONG);
                t.printStackTrace();
            }
        });
    }

    public void getIsArticleSubscribed() {
        Call<ResourceSubscriptionResponse> call = notificationService.getResourceSubscription(KhumuApplication.getUsername(), Integer.valueOf(articleID));
        call.enqueue(new Callback<ResourceSubscriptionResponse>() {
            @Override
            public void onResponse(Call<ResourceSubscriptionResponse> call, Response<ResourceSubscriptionResponse> response) {
                boolean isSubscribed = response.body().getData().isActivated;
                Log.d(TAG, "isArticleSubscribed : " + String.valueOf(isSubscribed));
                isArticleSubscribed.setValue(isSubscribed);
            }


            @Override
            public void onFailure(Call<ResourceSubscriptionResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void listComment() {
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

    public void createComment(SimpleComment comment) throws Exception{
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
                listComment();
            }
            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "댓글을 작성하지 못했습니다", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void deleteComment(int commentId) {
        Call<Comment> call = commentService.deleteComment("application/json", Integer.valueOf(commentId));
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                Log.d(TAG, "onResponse: " + response.code());
                Toast.makeText(context, "댓글을 삭제했습니다", Toast.LENGTH_LONG).show();
                listComment();
            }
            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "알 수 없는 이유로 삭제하지 못했습니다", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void likeComment(int commentId) throws Exception{
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