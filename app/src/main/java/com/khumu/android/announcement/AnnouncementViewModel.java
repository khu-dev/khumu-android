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
    public MutableLiveData<Boolean> showFollowedAnnouncement;
    private Context context;

    public AnnouncementViewModel(Context context, AnnouncementService announcementService) {
        this.context = context;
        this.announcementService = announcementService;
        announcements = new MutableLiveData<>();
        announcements.setValue(new ArrayList<Announcement>());
        showFollowedAnnouncement = new MutableLiveData<>();
        showFollowedAnnouncement.setValue(false);
        listAnnouncements();
        Log.d(TAG, "Created");
    }

    public void listAnnouncements() {
        Log.d(TAG, "listAnnouncements");
        Announcement test = Announcement.builder()
                .id(1)
                .title("안녕")
                .subLink("sdfdsf")
                .author(Announcement.AnnouncementAuthor.builder()
                        .followed(true)
                        .id(1)
                        .name("dizzi")
                        .build())
                .createdAt("2021-09-30")
                .build();
        Announcement test2 = Announcement.builder()
                .id(1)
                .title("안녕")
                .subLink("sdfdsf")
                .author(Announcement.AnnouncementAuthor.builder()
                        .followed(false)
                        .id(1)
                        .name("dizzi")
                        .build())
                .createdAt("2021-09-30")
                .build();
        List<Announcement> announcementTest = new ArrayList<Announcement>() {
            {
                add(test);
                add(test2);
                add(test);
                add(test2);
                add(test2);
                add(test);
                add(test);
                add(test2);
                add(test);
            }
        };
        announcements.setValue(announcementTest);
        showFollowedAnnouncement.postValue(false);
        /*
        Log.d(TAG, "listAnnouncements");
        Call<AnnouncementListResponse> call = announcementService.getAnnouncements();
        call.enqueue(new Callback<AnnouncementListResponse>() {
            @Override
            public void onResponse(Call<AnnouncementListResponse> call, Response<AnnouncementListResponse> response) {
                if (response.isSuccessful()) {
                    showFollowedAnnouncement.postValue(false);
                    announcements.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<AnnouncementListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        */
    }

    public void listFollowingAnnouncements() {
        showFollowedAnnouncement.postValue(true);
        /*
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

         */
    }

    public void searchAnnouncements(String searchWord) {
        Log.d(TAG, "searchBoards: " + searchWord);
        //TODO 검색 api 설계
    }

    public void showFollowedAnnouncement() {
        showFollowedAnnouncement.setValue(true);
    }

    public void showEntireAnnouncement() {
        showFollowedAnnouncement.setValue(false);
    }
}

