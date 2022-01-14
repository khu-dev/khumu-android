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
    Call<AnnouncementListResponse> getAnnouncements(@Query("userName") String user, @Query("page") int page);

    @POST("users/")
    Call<Announcement> postUser(@Query("userName") String user);

    @GET("announcements/user")
    Call<AnnouncementListResponse> getFollowingAnnouncements(@Query("userName") String user, @Query("page") int page);

    @GET("announcements/user")
    Call<AnnouncementListResponse> getFollowingAnnouncementsAtMyFeed(@Query("userName") String user, @Query("size") int size);
    @GET("announcements/search")
    Call<AnnouncementListResponse> searchAnnouncements(@Query("userName") String user,@Query("keyword") String keyword, @Query("page") int page);

    @GET("announcement/search")
    Call<AnnouncementListResponse> searchMoreAnnouncements(@Query("userName") String user, @Query("keyword") String keyword);

    @POST("follows/postFollow")
    Call<Announcement> followAuthor(@Query("userName") String user, @Query("authorName") String authorname);

    @DELETE("follows/deleteFollow")
    Call<Announcement> unFollowAuthor(@Query("userName") String user, @Query("authorName") String authorname);

}