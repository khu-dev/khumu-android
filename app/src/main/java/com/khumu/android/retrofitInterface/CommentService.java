package com.khumu.android.retrofitInterface;

import com.khumu.android.data.SimpleComment;

import org.w3c.dom.Comment;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommentService {
    @GET("comments")
    Call<CommentListResponse> getComments(@Query("article") int article);

    @POST("comments")
    Call<Comment> createComment(@Header("Content-Type") String contentType, @Body SimpleComment simpleComment);

    @DELETE("comment/{id}")
    Call<Comment> deleteComment(@Header("Content-Type") String contentType, @Path("id") int id, @Body Comment comment);
}
