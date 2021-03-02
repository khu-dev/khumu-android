package com.khumu.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.khumu.android.data.PushSubscription;
import com.khumu.android.data.rest.PushSubscriptionResponse;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.CommentRepository;
import com.khumu.android.repository.LikeArticleRepository;
import com.khumu.android.retrofitInterface.NotificationService;
import com.khumu.android.util.Util;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KhumuApplication extends Application {
    private final static String TAG = "KhumuApplication";
    private static String username;
    private static String nickname;
    private static String token;
    public static Container container;
    public static SharedPreferences sharedPref;
    @Inject
    public NotificationService notificationService;

    @Override
    public void onCreate() {
        super.onCreate();
        // 우리의 필요한 의존성들을 이 container에 Singleton으로 관리
        Util.init();
        container = DaggerContainer.create();
        container.inject(this);
        sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        // sharedPreferences를 이용해 필요한 데이터 초기화.
        loadKhumuConfig();
        Log.w(TAG, "onCreate: Token" + getToken());
        // FCM Push를 위해 초기화함.
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {// Get new FCM registration token
                        String token = task.getResult();
                        Log.d(TAG, "FCM Token: " + token);
                        Call<PushSubscriptionResponse>  call = notificationService.subscribe("application/json",
                                new PushSubscription(token));
                        call.enqueue(new Callback<PushSubscriptionResponse>() {
                            @Override
                            public void onResponse(Call<PushSubscriptionResponse> call, Response<PushSubscriptionResponse> response) {
                                if (response.isSuccessful()) {
                                    Log.i(TAG, "onResponse: " + response.body().getData());
                                } else{
                                    try {
                                        Log.e(TAG, "onResponse: " + response.errorBody().string());
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
                        Log.d(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                });



    }
    public static void loadKhumuConfig(){
        username = sharedPref.getString("username", null);
        nickname = sharedPref.getString("nickname", null);
        token = sharedPref.getString("token", null);
    }

    public static void setKhumuConfig(String username, String nickname, String token){
        SharedPreferences.Editor editor = KhumuApplication.sharedPref.edit();
        editor.putString("username", username);
        editor.putString("nickname", nickname);
        editor.putString("token", token);
        editor.commit();
    }

    public static void clearKhumuAuthenticationConfig(){
        SharedPreferences.Editor editor = KhumuApplication.sharedPref.edit();
        editor.remove("username");
        editor.remove("nickname");
        editor.remove("token");
        editor.commit();
    }

    public static boolean isAuthenticated(){
        return (username != null && username != "") && (nickname != null && nickname != "");
    }

    public static String getUsername() {
        return username;
    }

    public static String getNickname() {
        return nickname;
    }

    public static String getToken() {
        return token;
    }

    public static Container getContainer() {
        return container;
    }
}
