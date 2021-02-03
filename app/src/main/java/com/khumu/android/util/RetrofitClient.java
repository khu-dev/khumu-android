package com.khumu.android.util;

import android.util.Log;

import com.khumu.android.retrofitInterface.ArticleService;
import com.khumu.android.retrofitInterface.BoardService;
import com.khumu.android.retrofitInterface.ImageService;
import com.khumu.android.retrofitInterface.StorageService;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitClient {
    final static String TAG = "RetrofitClient";
    private static Retrofit retrofit = null;

    @Singleton
    @Provides
    public static Retrofit providesRetrofit() {
        Log.d(TAG, "providesRetrofit: ");
        retrofit = new Retrofit.Builder()
                .baseUrl(Util.APIRootEndpoint)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    @Singleton
    @Provides
    public static ArticleService getArticleService (Retrofit retrofit){
        Log.d(TAG, "getArticleService: ");
        return retrofit.create(ArticleService.class);
    }

    @Singleton
    @Provides
    public static BoardService getBoardService (Retrofit retrofit){
        Log.d(TAG, "getBoardService: ");
        return retrofit.create(BoardService.class);
    }

    @Singleton
    @Provides
    public static ImageService getImageService (Retrofit r){
        Log.d(TAG, "getImageService: ");
        return r.create(ImageService.class);
    }

    @Singleton
    @Provides
    public static StorageService getStorageService (Retrofit r){
        return r.create(StorageService.class);
    }
}