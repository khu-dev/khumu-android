package com.khumu.android.retrofitInterface;

import com.khumu.android.data.rest.BoardListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface BoardService {

    @GET("boards")
    Call<BoardListResponse> getBoardsByCategory (@Header("Authorization") String authorization, @Query("category") String category);

    @GET("boards")
    Call<BoardListResponse> getFollowingBoards (@Header("Authorization") String authorization, @Query("followed") String followed);

    @GET("boards")
    Call<BoardListResponse> getBoards();
}
