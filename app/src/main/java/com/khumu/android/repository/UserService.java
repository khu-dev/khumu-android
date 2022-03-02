package com.khumu.android.repository;

import com.khumu.android.data.Comment;
import com.khumu.android.data.Info21UserInfo;
import com.khumu.android.data.KhumuUser;
import com.khumu.android.data.SimpleComment;
import com.khumu.android.data.rest.DefaultResponse;
import com.khumu.android.data.rest.Info21AuthenticationRequest;
import com.khumu.android.data.rest.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserService {

    @POST("users")
    Call<UserResponse> signUp(@Header("Content-Type") String contentType, @Body KhumuUser user);

    @POST("users/verify-new-student")
    Call<DefaultResponse<Info21UserInfo>> verifyNewStudent(@Header("Content-Type") String contentType, @Body Info21AuthenticationRequest input);

    @POST("users/access")
    Call<DefaultResponse<String>> access(@Header("Content-Type") String contentType);
}
