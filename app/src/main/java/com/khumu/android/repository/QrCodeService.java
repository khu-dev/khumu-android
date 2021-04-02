package com.khumu.android.repository;

import com.khumu.android.data.rest.ImageUploadResponse;
import com.khumu.android.data.rest.QrCodeGetResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface QrCodeService {
    @GET("qr-code")
    Call<QrCodeGetResponse> getQrCode ();
}
