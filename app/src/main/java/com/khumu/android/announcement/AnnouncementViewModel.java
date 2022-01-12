package com.khumu.android.announcement;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Announcement;
import com.khumu.android.data.rest.AnnouncementListResponse;
import com.khumu.android.repository.AnnouncementService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnnouncementViewModel extends ViewModel {

    private final static String TAG = "AnnouncementViewModel";
    public AnnouncementService announcementService;
    public MutableLiveData<List<Announcement>> announcements;
    public MutableLiveData<List<Announcement>> followingAnnouncements;
    public MutableLiveData<Boolean> showFollowedAnnouncement;
    private Context context;

    public AnnouncementViewModel(Context context, AnnouncementService announcementService) {
        this.context = context;
        this.announcementService = announcementService;
        announcements = new MutableLiveData<>();
        announcements.setValue(new ArrayList<Announcement>());
        showFollowedAnnouncement = new MutableLiveData<>();
        showFollowedAnnouncement.setValue(false);
        //listFollowingAnnouncements();
        listAnnouncements();
        Log.d(TAG, "Created");
    }

    public void followAuthor(String authorName) {
        Log.d(TAG, "followAuthor");
        Call<Announcement> call = announcementService.followAuthor(KhumuApplication.getUsername(), authorName);
        call.enqueue(new Callback<Announcement>() {
            @Override
            public void onResponse(Call<Announcement> call, Response<Announcement> response) {
                if (response.isSuccessful()) {
                    System.out.println("follow success");
                }
            }

            @Override
            public void onFailure(Call<Announcement> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void unFollowAuthor(String authorName) {
        Log.d(TAG, "unFollowAuthor");
        Call<Announcement> call = announcementService.unFollowAuthor(KhumuApplication.getUsername(), authorName);
        call.enqueue(new Callback<Announcement>() {
            @Override
            public void onResponse(Call<Announcement> call, Response<Announcement> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "언팔로우 성공");
                }
            }

            @Override
            public void onFailure(Call<Announcement> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void listAnnouncements() {
        Log.d(TAG, "listAnnouncements");
        showFollowedAnnouncement.postValue(false);
        Call<AnnouncementListResponse> call = announcementService.getAnnouncements(KhumuApplication.getUsername());
        call.enqueue(new Callback<AnnouncementListResponse>() {
            @Override
            public void onResponse(Call<AnnouncementListResponse> call, Response<AnnouncementListResponse> response) {
                if (response.isSuccessful()) {
                    showFollowedAnnouncement.postValue(false);
                    System.out.println("announcments : "+response.body());
                    announcements.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<AnnouncementListResponse> call, Throwable t) {
                System.out.println("announcments:"+call.toString());
                t.printStackTrace();
            }
        });
    }

    public void listFollowingAnnouncements() {
        showFollowedAnnouncement.postValue(true);
        Log.d(TAG, "listFollowingAnnouncements");
        Call<AnnouncementListResponse> call = announcementService.getFollowingAnnouncements(KhumuApplication.getUsername());
        call.enqueue(new Callback<AnnouncementListResponse>() {
            @Override
            public void onResponse(Call<AnnouncementListResponse> call, Response<AnnouncementListResponse> response) {
                if (response.isSuccessful()) {
                    showFollowedAnnouncement.postValue(true);
                    announcements.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<AnnouncementListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void searchAnnouncements(String keyword) {
        Log.d(TAG, "searchBoards: " + keyword);
        //TODO 검색 api 설계
        Call<AnnouncementListResponse> call = announcementService.searchAnnouncements(KhumuApplication.getUsername(), keyword);
        call.enqueue(new Callback<AnnouncementListResponse>() {
            @Override
            public void onResponse(Call<AnnouncementListResponse> call, Response<AnnouncementListResponse> response) {
                if (response.isSuccessful()) {
                    showFollowedAnnouncement.postValue(false);
                    System.out.println("announcments : "+response.body());
                    announcements.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<AnnouncementListResponse> call, Throwable t) {
                System.out.println("announcments: "+call.toString());
                t.printStackTrace();
            }
        });
    }

    public void showFollowedAnnouncement() {
        showFollowedAnnouncement.setValue(true);
    }

    public void showEntireAnnouncement() {
        showFollowedAnnouncement.setValue(false);
    }
}

