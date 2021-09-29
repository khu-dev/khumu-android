package com.khumu.android.repository;

import com.khumu.android.data.rest.AnnouncementListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AnnouncementService {
    @GET("announcements/all")
    Call<AnnouncementListResponse> getAnnouncements();

    @GET("announcements/user")
    Call<AnnouncementListResponse> getAnnouncementByUser(@Query("user") String user);

    @GET("announcements/authorname")
    Call<AnnouncementListResponse> getFollowingAnnouncements(@Query("authorname") String authorname);

}
