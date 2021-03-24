package com.khumu.android.repository;

import com.khumu.android.data.Comment;
import com.khumu.android.data.KhumuUser;
import com.khumu.android.data.SimpleComment;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserService {

    @POST("users")
    Call<KhumuUser> signUp(@Header("Content-Type") String contentType, @Body KhumuUser user);
}
