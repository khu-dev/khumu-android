/**
 * 이미지 자원에 대한 Get 요청
 * https://storage.khumu.jinsu.me 를 기준으로 함.
 */
package com.khumu.android.retrofitInterface;

import com.khumu.android.data.rest.ImageUploadResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface StorageService {
    @POST("{filePath}")
    @Multipart
    Call<ImageUploadResponse> uploadImage ();
}
