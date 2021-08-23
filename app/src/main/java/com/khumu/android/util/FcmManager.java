package com.khumu.android.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.khumu.android.data.PushSubscription;
import com.khumu.android.data.rest.PushSubscriptionResponse;
import com.khumu.android.repository.NotificationService;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * FCM 관련 작업들을 수행한다.
 */
@Singleton
public class FcmManager extends FirebaseMessagingService {
    private final static String TAG = "FcmManager";
    private final NotificationService notificationService;

    @Inject
    public FcmManager(NotificationService notificationService) {
        Log.d(TAG, "FcmManager: ");
        this.notificationService = notificationService;
    }

    public void createOrUpdatePushSubscription() {
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {// Get new FCM registration token
                        String token = task.getResult();
                        Call<PushSubscriptionResponse> call = notificationService.subscribe("application/json",
                                new PushSubscription(token));
                        call.enqueue(new Callback<PushSubscriptionResponse>() {
                            @Override
                            public void onResponse(Call<PushSubscriptionResponse> call, Response<PushSubscriptionResponse> response) {
                                if (response.isSuccessful()) {
                                    Log.d(TAG, "PushSubscription onResponse: " + response.body().getData());
                                } else{
                                    try {
                                        Log.e(TAG, "PushSubscription onResponse: " + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<PushSubscriptionResponse> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    } else {
                        Log.e(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                });
    }
}
