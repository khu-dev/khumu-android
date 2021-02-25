package com.khumu.android.articleDetail;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Article;
import com.khumu.android.data.Comment;
import com.khumu.android.data.SimpleComment;
import com.khumu.android.data.rest.CommentListResponse;
import com.khumu.android.repository.CommentRepository;
import com.khumu.android.retrofitInterface.CommentService;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CommentViewModel extends ViewModel {

    private final static String TAG = "CommentViewModel";
    public CommentService commentService;
    private MutableLiveData<ArrayList<Comment>> comments;
    //article은 변하는 값을 observe할 데이터가 아니라 MutableLiveData로 하지 않아도 된다.
    private Article article;
    private String articleID;
    public CommentViewModel(CommentService commentService, Article article, String articleID) {
        this.commentService = commentService;
        comments = new MutableLiveData<>();
        comments.setValue(new ArrayList<Comment>());
        //this.commentRepository = commentRepository;
        this.articleID = articleID;

        this.article = article;
        ListComment();
    }

    public Article getArticle() {
        return article;
    }

    public MutableLiveData<ArrayList<Comment>> getLiveDataComments(){
        return comments;
    }

    public void ListComment() {
        ArrayList<Comment> originalComments = comments.getValue();
        System.out.println(comments);
        Call<CommentListResponse> call = commentService.getComments(Integer.valueOf(articleID));
        call.enqueue(new Callback<CommentListResponse>() {
            @Override
            public void onResponse(Call<CommentListResponse> call, Response<CommentListResponse> response) {
                Log.d(TAG, String.valueOf(response.code()));
                List<Comment> tempList = response.body().getData();
                ArrayList<Comment> commentsList = new ArrayList<>();
                commentsList.addAll(tempList);
                System.out.println(commentsList);
                comments.postValue(commentsList);
            }

            @Override
            public void onFailure(Call<CommentListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
//                    for (Comment newComment : commentRepository.ListComment(articleID)) {
//                        // 기존에 없던 새로운 comment인지 확인
//                        List<Comment> duplicatedComments = originalComments.stream().filter(item->{
//                            return (newComment.getId() == item.getId());
//                        }).collect(Collectors.toList());
//                        if(duplicatedComments.size() == 0) {
//                            originalComments.add(newComment);
//                        }
//                        else{
//                        }
//                    }
//                    comments.postValue(originalComments);
    }

    public void CreateComment(SimpleComment comment) throws Exception{
        Call<Comment> call = commentService.createComment("application/json", comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                Log.d(TAG, "onResponse: " + response.code());
            }
            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                t.printStackTrace();
            }
        });

//        OkHttpClient client = new OkHttpClient();
//        ObjectMapper mapper = new ObjectMapper();
//        String commentString = mapper.writeValueAsString(comment);
//        JSONObject commentJSON = new JSONObject(commentString);
//        RequestBody authBody = RequestBody.create(MediaType.parse("application/json"),
//                String.format("{\"username\":\"%s\",\"password\":\"%s\"}", Util.DEFAULT_USERNAME, Util.DEFAULT_PASSWORD)
//        );
//        Request authReq = new Request.Builder()
//                .post(authBody)
//                .url(Util.APIRootEndpoint + "token")
//                .build();
//        Response authResp = client.newCall(authReq).execute();
//        String authRespStr = authResp.body().string();
//        String token = new JSONObject(authRespStr).getString("access");
//
//        Request createReq = new Request.Builder()
//                .header("Authorization", "Bearer "+token)
//                .post(RequestBody.create(MediaType.parse("application/json"), commentString))
//                // 임시
//                .url(Util.APIRootEndpoint + "comments" + "?articles=1")
//                .build();
//        Response createResp = client.newCall(createReq).execute();
//        String createRespStr = createResp.body().string();
//        System.out.println("createRespStr: " + createRespStr);
    }

    public void DeleteComment() {
        Call<Comment> call = commentService.deleteComment("application/json", Integer.valueOf(articleID));
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                Log.d(TAG, "onResponse: " + response.code());
            }
            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}