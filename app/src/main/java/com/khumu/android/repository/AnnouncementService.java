package com.khumu.android.repository;

import com.khumu.android.data.Announcement;
import com.khumu.android.data.rest.AnnouncementListResponse;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AnnouncementService {
    @GET("announcements/all")
    Call<AnnouncementListResponse> getAnnouncements();
//
//    @GET("announcements/user")
//    Call<AnnouncementListResponse> getAnnouncementByUser(@Query("user") String user);

    @GET("announcements/user")
    Call<AnnouncementListResponse> getFollowingAnnouncements(@Query("user") String user);

    @GET("announcements/search")
    Call<AnnouncementListResponse> searchAnnouncements(@Query("keyword") String keyword);

    @POST("follows/postFollow")
    Call<Announcement> followAuthor(@Query("userName") String user, @Query("authorName") String authorname);

    @DELETE("follows/deleteFollow")
    Call<Announcement> unFollowAuthor(@Query("userName") String user, @Query("authorName") String authorname);

}
