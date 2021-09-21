package com.khumu.android.announcement;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Announcement;
import com.khumu.android.data.rest.AnnouncementListResponse;
import com.khumu.android.repository.AnnouncementService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnnouncementViewModel {

    private final static String TAG = "AnnouncementViewModel";
    public AnnouncementService announcementService;
    public MutableLiveData<List<Announcement>> announcements;
    public MutableLiveData<Boolean> showFollowedAnnouncement;
    private Context context;

    public AnnouncementViewModel(Context context, AnnouncementService announcementService) {
        this.context = context;
        this.announcementService = announcementService;
        announcements = new MutableLiveData<>();
        announcements.setValue(new ArrayList<Announcement>());
        showFollowedAnnouncement = new MutableLiveData<>();
        showFollowedAnnouncement.setValue(false);
        Log.d(TAG, "Created");
    }

    public void listAnnouncements() {
        Log.d(TAG, "listAnnouncements");
        Call<AnnouncementListResponse> call = announcementService.getAnnouncements();
        call.enqueue(new Callback<AnnouncementListResponse>() {
            @Override
            public void onResponse(Call<AnnouncementListResponse> call, Response<AnnouncementListResponse> response) {
                if (response.isSuccessful()) {
                    announcements.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<AnnouncementListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void listFollowingBoards() {
        Log.d(TAG, "listFollowingBoards");
        Call<AnnouncementListResponse> call = announcementService.getFollowingAnnouncements(KhumuApplication.getUsername());
        call.enqueue(new Callback<AnnouncementListResponse>() {
            @Override
            public void onResponse(Call<AnnouncementListResponse> call, Response<AnnouncementListResponse> response) {
                if (response.isSuccessful()) {
                    announcements.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<AnnouncementListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void searchBoards() {
        Log.d(TAG, "searchBoards");
        //TODO api설계
    }

    public void showFollowedAnnouncement() {
        showFollowedAnnouncement.setValue(true);
    }

    public void showEntireAnnouncement() {
        showFollowedAnnouncement.setValue(false);
    }
}

