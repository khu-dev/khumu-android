package com.khumu.android.repository;

import com.khumu.android.data.rest.JWTRequest;
import com.khumu.android.data.rest.JWTResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TokenService {
    @POST("token")
    Call<JWTResponse> postToken(@Header("Content-Type") String contentType, @Body JWTRequest authString);

}
