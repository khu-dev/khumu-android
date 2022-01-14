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
    private int pageNumber;
    private boolean isLastAnnouncement;

    public AnnouncementViewModel(Context context, AnnouncementService announcementService) {
        this.context = context;
        this.announcementService = announcementService;
        announcements = new MutableLiveData<>();
        announcements.setValue(new ArrayList<Announcement>());
        showFollowedAnnouncement = new MutableLiveData<>();
        showFollowedAnnouncement.setValue(false);
        pageNumber = 0;
        isLastAnnouncement = false;
        //listFollowingAnnouncements();
        listAnnouncements();
        Log.d(TAG, "Created");
    }

    public void refreshAnnouncement() {
        pageNumber = 0;
        isLastAnnouncement = false;
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
        showFollowedAnnouncement.postValue(false);
        Log.d(TAG, "listAnnouncements?page=" + pageNumber);
         showFollowedAnnouncement.postValue(false);
         if(!isLastAnnouncement) {
             Call<AnnouncementListResponse> call = announcementService.getAnnouncements(KhumuApplication.getUsername(), pageNumber);
             call.enqueue(new Callback<AnnouncementListResponse>() {
                 @Override
                 public void onResponse(Call<AnnouncementListResponse> call, Response<AnnouncementListResponse> response) {
                     if (response.isSuccessful()) {
                         if(response.body().size() < 10) {
                             isLastAnnouncement = true;
                         }
                         if (pageNumber == 0) {
                             announcements.postValue(response.body());
                         } else {
                             List<Announcement> temp = announcements.getValue();
                             temp.addAll(response.body());
                             announcements.postValue(temp);
                         }
                         pageNumber++;
                     }
                 }

                 @Override
                 public void onFailure(Call<AnnouncementListResponse> call, Throwable t) {
                     t.printStackTrace();
                 }
             });
         }
    }

    public void listFollowingAnnouncements() {
        showFollowedAnnouncement.postValue(true);
        Log.d(TAG, "listFollowingAnnouncements?page" + pageNumber);
        if(!isLastAnnouncement) {
            Call<AnnouncementListResponse> call = announcementService.getFollowingAnnouncements(KhumuApplication.getUsername(), pageNumber);
            call.enqueue(new Callback<AnnouncementListResponse>() {
                @Override
                public void onResponse(Call<AnnouncementListResponse> call, Response<AnnouncementListResponse> response) {
                    if (response.isSuccessful()) {
                        if(response.body().size() < 10) {
                            System.out.println("lastAnnouncement");
                            isLastAnnouncement = true;
                        }
                        List<Announcement> newAnnouncements = new ArrayList<>();
                        if(!announcements.getValue().isEmpty()) {
                            for (Announcement announcement : response.body()) {
                                announcement.author.followed = true;
                                newAnnouncements.add(announcement);
                            }
                        }
                        if (pageNumber == 0) {
                            showFollowedAnnouncement.postValue(true);
                            announcements.postValue(newAnnouncements);
                        } else {
                            List<Announcement> temp = announcements.getValue();
                            temp.addAll(newAnnouncements);
                            announcements.postValue(temp);
                        }
                        pageNumber++;
                    }
                }

                @Override
                public void onFailure(Call<AnnouncementListResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public void searchAnnouncements(String keyword) {
        Log.d(TAG, "searchBoards: " + keyword);
        if(!isLastAnnouncement) {
            Call<AnnouncementListResponse> call = announcementService.searchAnnouncements(KhumuApplication.getUsername(), keyword, pageNumber);
            call.enqueue(new Callback<AnnouncementListResponse>() {
                @Override
                public void onResponse(Call<AnnouncementListResponse> call, Response<AnnouncementListResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "searchAnnouncements?page" + pageNumber);
                        if(response.body().size() < 10) {
                            System.out.println("lastAnnouncement");
                            isLastAnnouncement = true;
                        }
                        if (pageNumber == 0) {
                            announcements.postValue(response.body());
                        } else {
                            List<Announcement> temp = announcements.getValue();
                            temp.addAll(response.body());
                            announcements.postValue(temp);
                        }
                        pageNumber++;
                    }
                }

                @Override
                public void onFailure(Call<AnnouncementListResponse> call, Throwable t) {
                    System.out.println("announcments: "+call.toString());
                    t.printStackTrace();
                }
            });
        }
    }

    public void showFollowedAnnouncement() {
        showFollowedAnnouncement.setValue(true);
    }

    public void showEntireAnnouncement() {
        showFollowedAnnouncement.setValue(false);
    }
}

