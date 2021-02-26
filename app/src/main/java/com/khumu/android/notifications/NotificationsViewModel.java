package com.khumu.android.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Notification;
import com.khumu.android.data.rest.NotificationListResponse;
import com.khumu.android.retrofitInterface.NotificationService;

import java.io.IOException;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
@Setter
public class NotificationsViewModel extends ViewModel {
    private NotificationService notificationService;
    private MutableLiveData<List<Notification>> notifications;

    public NotificationsViewModel(NotificationService notificationService, List<Notification> notifications) {
        this.notificationService = notificationService;
        this.notifications = new MutableLiveData<>(notifications);
    }

    public void listNotifications(){
        Call<NotificationListResponse> call = notificationService.getNotifications(KhumuApplication.getUsername());
        call.enqueue(new Callback<NotificationListResponse>() {
            @Override
            public void onResponse(Call<NotificationListResponse> call, Response<NotificationListResponse> response) {
                List<Notification> results = response.body().getData();
                for (Notification n : results) {
                    System.out.println("NotificationsViewModel.onResponse");
                }
                notifications.postValue(results);
            }

            @Override
            public void onFailure(Call<NotificationListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}