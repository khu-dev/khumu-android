package com.khumu.android.articleDetail;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.CommentRepository;
import com.khumu.android.util.Util;
import com.khumu.android.data.Comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CommentViewModel extends ViewModel {
    private CommentRepository commentRepository;
    private MutableLiveData<ArrayList<Comment>> comments;
    private MutableLiveData<String> articlesID;

    public CommentViewModel(CommentRepository commentRepository) {
        comments = new MutableLiveData<>();
        comments.setValue(new ArrayList<Comment>());
        this.commentRepository = commentRepository;

    }

    public MutableLiveData<ArrayList<Comment>> getLiveDataComments(){
        return comments;
    }

    public void ListComment() {
        new Thread() {
            @Override
            public void run() {
                try {
                    ArrayList<Comment> originalComments = comments.getValue();
                    for (Comment newComment : commentRepository.ListComment()) {
                        // 기존에 없던 새로운 comment인지 확인
                        List<Comment> duplicatedComments = originalComments.stream().filter(item->{
                            return (newComment.getID().equals(item.getID()));
                        }).collect(Collectors.toList());
                        if(duplicatedComments.size() == 0) {
                            originalComments.add(newComment);
                        }
                        else{

                        }
                    }
                    comments.postValue(originalComments);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void CreateComment(Comment comment) throws Exception{
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        String commentString = mapper.writeValueAsString(comment);
        JSONObject commentJSON = new JSONObject(commentString);
        System.out.println(commentString);
        RequestBody authBody = RequestBody.create(MediaType.parse("application/json"),
                String.format("{\"username\":\"%s\",\"password\":\"%s\"}", Util.DEFAULT_USERNAME, Util.DEFAULT_PASSWORD)
        );
        Request authReq = new Request.Builder()
                .post(authBody)
                .url(Util.APIRootEndpoint + "token")
                .build();
        Response authResp = client.newCall(authReq).execute();
        String authRespStr = authResp.body().string();
        String token = new JSONObject(authRespStr).getString("access");

        Request createReq = new Request.Builder()
                .header("Authorization", "Bearer "+token)
                .post(RequestBody.create(MediaType.parse("application/json"), commentString))
                // 임시
                .url(Util.APIRootEndpoint + "comments" + "?articles=1")
                .build();
        Response createResp = client.newCall(createReq).execute();
        String createRespStr = createResp.body().string();
        System.out.println("createRespStr: " + createRespStr);
    }
}