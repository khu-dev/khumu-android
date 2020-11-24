package com.khumu.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.CommentRepository;
import com.khumu.android.repository.LikeArticleRepository;
import com.khumu.android.util.Util;

import java.util.ArrayList;

import javax.inject.Inject;

public class KhumuApplication extends Application {
    public static String username;
    public static String nickname;
    public static String token;
    public static Container container;
    public static SharedPreferences sharedPref;

    @Override
    public void onCreate() {
        super.onCreate();
        // 우리의 필요한 의존성들을 이 container에 Singleton으로 관리
        container = DaggerContainer.create();
        Context context = getApplicationContext();
        sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        // sharedPreferences를 이용해 필요한 데이터 초기화.
        loadKhumuConfig();
    }
    public static void loadKhumuConfig(){
        username = sharedPref.getString("username", null);
        nickname = sharedPref.getString("nickname", null);
        token = sharedPref.getString("token", null);
    }
}
