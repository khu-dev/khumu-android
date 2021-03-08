package com.khumu.android.repository;

import com.khumu.android.data.Comment;
import com.khumu.android.data.SimpleComment;
import com.khumu.android.data.rest.CommentListResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommentService {
    @GET("comments")
    Call<CommentListResponse> getComments(@Query("article") int article);

    @POST("comments")
    Call<Comment> createComment(@Header("Content-Type") String contentType, @Body SimpleComment simpleComment);

    @DELETE("comments/{id}")
    Call<Comment> deleteComment(@Header("Content-Type") String contentType, @Path("id") int id);

    @PATCH("comments/{id}/likes")
    Call<Comment> likeComment(@Header("Content-Type") String contentType, @Path("id") int id);
}
