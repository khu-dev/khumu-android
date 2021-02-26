package com.khumu.android.util;

import android.util.Log;

import com.khumu.android.data.Notification;
import com.khumu.android.retrofitInterface.ArticleService;
import com.khumu.android.retrofitInterface.BoardService;
import com.khumu.android.retrofitInterface.ImageService;
import com.khumu.android.retrofitInterface.NotificationService;
import com.khumu.android.retrofitInterface.TokenService;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {
    final static String TAG = "RetrofitModule";

    @Singleton
    @Provides
    public static Retrofit providesRetrofit(OkHttpClient okHttpClient) {
        Log.d(TAG, "providesRetrofit: ");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Util.APIRootEndpoint)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }

    @Singleton
    @Provides
    public static OkHttpClient providesOkHttpClient(AuthenticationInterceptor authenticationInterceptor) {
        Log.d(TAG, "providesOkHttpClient");
        return new OkHttpClient().newBuilder()
                .addInterceptor(authenticationInterceptor)
                .build();
    }

    @Singleton
    @Provides
    public static AuthenticationInterceptor providesAuthenticationInterceptor() {
        Log.d(TAG, "providesAuthenticationInterceptor");
        return new AuthenticationInterceptor();
    }

    @Singleton
    @Provides
    public static ArticleService providesArticleService (Retrofit retrofit){
        Log.d(TAG, "providesArticleService: ");
        return retrofit.create(ArticleService.class);
    }

    @Singleton
    @Provides
    public static BoardService providesBoardService (Retrofit retrofit){
        Log.d(TAG, "providesBoardService: ");
        return retrofit.create(BoardService.class);
    }

    @Singleton
    @Provides
    public static ImageService providesImageService (Retrofit retrofit){
        Log.d(TAG, "providesImageService: ");
        return retrofit.create(ImageService.class);
    }

    @Singleton
    @Provides
    public static TokenService providesTokenService (Retrofit retrofit) {
        return retrofit.create(TokenService.class);
    }

    @Singleton
    @Provides
    public static NotificationService providesNotificationService (Retrofit retrofit) {
        return retrofit.create(NotificationService.class);
    }
}