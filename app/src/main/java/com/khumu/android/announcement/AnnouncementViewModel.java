package com.khumu.android.announcement;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.khumu.android.data.Announcement;
import com.khumu.android.repository.AnnouncementService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class AnnouncementViewModel {

    private final static String TAG = "AnnouncementViewModel";
    public AnnouncementService announcementService;
    public MutableLiveData<List<Announcement>> announcements;
    private Context context;

    public AnnouncementViewModel(Context context, AnnouncementService announcementService) {
        this.context = context;
        this.announcementService = announcementService;
        announcements = new MutableLiveData<>();
        announcements.setValue(new ArrayList<Announcement>());
        Log.d(TAG, "Created");
    }

    public void listAnnouncements() {
        Log.d(TAG, "listAnnouncements");
        Call<Announcement>
    }

    public void listFollowingBoards() {
        Log.d(TAG, "listFollowingBoards");
    }
}

