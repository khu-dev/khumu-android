package com.khumu.android.retrofitInterface;

import com.khumu.android.data.rest.BoardListResponse;
import com.khumu.android.data.rest.ImageUploadResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ImageService {
    @POST("images")
    @Multipart
    Call<ImageUploadResponse> uploadImage (@Header("Authorization") String authorization, @Part MultipartBody.Part image);
}
