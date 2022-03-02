package com.khumu.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.khumu.android.data.PushSubscription;
import com.khumu.android.data.rest.DefaultResponse;
import com.khumu.android.data.rest.PushSubscriptionResponse;
import com.khumu.android.di.component.ApplicationComponent;
import com.khumu.android.di.component.DaggerApplicationComponent;
import com.khumu.android.repository.NotificationService;
import com.khumu.android.repository.UserService;
import com.khumu.android.util.FcmManager;
import com.khumu.android.util.Util;
import com.thefinestartist.finestwebview.FinestWebView;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KhumuApplication extends Application {
    private final static String TAG = "KhumuApplication";
    private static String username = null;
    private static String nickname = null;
    private static String token = null;
    private static String pushToken = null;
    public static ApplicationComponent applicationComponent;
    public static SharedPreferences sharedPref;
    @Inject
    public UserService userService;
    @Inject
    public NotificationService notificationService;
    @Inject
    public FcmManager fcmManager;


    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "onCreate: 애플리케이션 시작");
        // 우리의 필요한 의존성들을 이 container에 Singleton으로 관리
        applicationComponent = DaggerApplicationComponent.create();
        applicationComponent.inject(this);
        sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        // sharedPreferences를 이용해 필요한 데이터 초기화.
        loadKhumuConfig();
//        clearKhumuAuthenticationConfig(); // 디버깅하느라 로그아웃이 필요할 때
        fcmManager.createOrUpdatePushSubscription();
        // FCM Push를 위해 초기화함.
        userService.access("application/json").enqueue(new Callback<DefaultResponse<String>>() {
            @Override
            public void onResponse(Call<DefaultResponse<String>> call, Response<DefaultResponse<String>> response) {
                Log.d(TAG, "succeeded to create access log");
            }

            @Override
            public void onFailure(Call<DefaultResponse<String>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void loadKhumuConfig(){
        username = sharedPref.getString("username", null);
        nickname = sharedPref.getString("nickname", null);
        token = sharedPref.getString("token", null);
        pushToken = sharedPref.getString("pushToken", null);
    }

    public static void setKhumuConfig(String username, String nickname, String token, String pushToken){
        SharedPreferences.Editor editor = KhumuApplication.sharedPref.edit();
        editor.putString("username", username);
        editor.putString("nickname", nickname);
        editor.putString("token", token);
        editor.putString("pushToken", pushToken);
        editor.commit();
    }

    public static void clearKhumuAuthenticationConfig(){
        SharedPreferences.Editor editor = KhumuApplication.sharedPref.edit();
        editor.remove("username");
        editor.remove("nickname");
        editor.remove("token");
        editor.remove("pushToken");
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
    public static String getPushToken() {
        return pushToken;
    }

    public static ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
