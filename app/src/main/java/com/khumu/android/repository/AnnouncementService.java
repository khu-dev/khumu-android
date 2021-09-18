package com.khumu.android.repository;

import com.khumu.android.data.rest.AnnouncementListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AnnouncementService {
    @GET("announcement/all")
    Call<AnnouncementListResponse> getAnnouncements();

    @GET("announcement")
    Call<AnnouncementListResponse> getAnnouncementByAuthor(@Query("author_name") String authorName);

    @GET("announcements")
    Call<AnnouncementListResponse> getFollowingAnnouncements(@Header("user") String userName);

}
