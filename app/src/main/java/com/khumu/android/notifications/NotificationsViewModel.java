package com.khumu.android.notifications;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Notification;
import com.khumu.android.data.rest.NotificationListResponse;
import com.khumu.android.data.rest.NotificationReadRequest;
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
    private final String TAG = "NotificationsViewModel";

    public NotificationsViewModel(NotificationService notificationService, List<Notification> notifications) {
        this.notificationService = notificationService;
        this.notifications = new MutableLiveData<>(notifications);
    }

    public void listNotifications(){
        Call<NotificationListResponse> call = notificationService.getNotifications(KhumuApplication.getUsername());
        call.enqueue(new Callback<NotificationListResponse>() {
            @Override
            public void onResponse(Call<NotificationListResponse> call, Response<NotificationListResponse> response) {
                if (response.code() == 200 && response.body().getData() != null) {
                    List<Notification> results = response.body().getData();
                    for (Notification n : results) {
                        if (!n.isRead()) {
                            //mockReadNotification();
                            readNotification(n.getId());
                        }
                    }
                    notifications.postValue(results);
                } else {
                    Log.e(TAG, "onResponse: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<NotificationListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void mockReadNotification(Long id) {
        Log.w(TAG, "mockReadNotification: " + "이건 그냥 Mocking일 뿐.");
    }

    public void readNotification(Long id) {
//        notificationService.
        Log.d(TAG, "readNotification " + id);
        Call<Object> call = notificationService.patchRead("application/json", id, new NotificationReadRequest(true));
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.code() == 200) {
                    // success
                    Log.d(TAG, "onResponse: readNotification success");
                } else {
                    Log.e(TAG, "onResponse: readNotification failed");
                    try {
                        Log.e(TAG, "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}