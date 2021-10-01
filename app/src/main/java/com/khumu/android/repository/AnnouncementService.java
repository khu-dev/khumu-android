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

    //TODO 전체 보드의 공지사항 중 검색어 포함된 공지사항 불러오기

    //TODO 팔로우한 보드의 공지사항 중 검색어 포함된 공지사항 불러오기

}
