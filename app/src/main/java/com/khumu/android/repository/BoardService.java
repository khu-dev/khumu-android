package com.khumu.android.repository;

import com.khumu.android.data.Board;
import com.khumu.android.data.rest.BoardListResponse;

import retrofit2.Response;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BoardService {

    @POST("boards/{name}/follows")
    Call<Board> followBoard(@Header("Content-Type") String contentType, @Path("name") String name);

    @DELETE("boards/{name}/follows")
    Call<Board> unfollowBoard(@Header("Content-Type") String contentType, @Path("name") String name);

    @GET("boards")
    Call<BoardListResponse> getUnFollowingBoardsByCategory(@Header("Content-Type") String contentType, @Query("category") String category, @Query("followed") boolean followed);

    @GET("boards")
    Call<BoardListResponse> getBoardsByCategory (@Header("Content-Type") String contentType, @Query("category") String category);

    @GET("boards")
    Call<BoardListResponse> getFollowingBoards (@Header("Content-Type") String contentType, @Query("followed") boolean followed);

    @GET("boards")
    Call<BoardListResponse> getUnFollowingRandomBoard (@Header("Content-Type") String contentType, @Query("random") boolean random, @Query("followed") boolean followed, @Query("category") String category);

    @GET("boards")
    Call<BoardListResponse> getBoards();
}
