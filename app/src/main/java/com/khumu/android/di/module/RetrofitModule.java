package com.khumu.android.di.module;

import android.util.Log;

import com.khumu.android.di.util.AuthenticationInterceptor;
import com.khumu.android.repository.ArticleService;
import com.khumu.android.repository.BoardService;
import com.khumu.android.repository.CommentService;
import com.khumu.android.repository.ImageService;
import com.khumu.android.repository.NotificationService;
import com.khumu.android.repository.TokenService;
import com.khumu.android.util.Util;

import javax.inject.Singleton;

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
    public static CommentService providesCommentService (Retrofit retrofit) {
        return retrofit.create(CommentService.class);
    }

    @Singleton
    @Provides
    public static NotificationService providesNotificationService (Retrofit retrofit) {
        return retrofit.create(NotificationService.class);
    }
}