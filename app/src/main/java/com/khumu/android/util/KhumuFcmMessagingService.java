package com.khumu.android.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class KhumuFcmMessagingService extends FirebaseMessagingService {

    private final static String TAG = "KhumuFcmMessagingService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.i(TAG, "onMessageReceived: " + remoteMessage.getData().get("link"));
    }
}
