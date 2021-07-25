package com.khumu.android.repository;

import com.khumu.android.data.StudyArticle;
import com.khumu.android.data.rest.StudyListResponse;

import dagger.Provides;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StudyService {
    @GET("study_fields")
    Call<StudyListResponse> getStudies(@Query("page") int page);

    @GET("study_fields/{id}")
    Call<StudyArticle> getStudy(@Path("id") int id);

    @POST("study_fields")
    Call<Object> createStudy(@Header("Content-Type") String contentType, @Body StudyArticle study);

    @PATCH("study_fields/{id}")
    Call<Object> updateStudy(@Header("Content-Type") String contentType, @Path("id") int id, @Body StudyArticle study);

    @DELETE("study_fields/{id}")
    Call<Object> deleteArticle(@Header("Content-Type") String contentType, @Path("id") int id);
}
