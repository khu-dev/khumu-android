package com.khumu.android.util;

import android.util.Log;

import com.khumu.android.KhumuApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {
    private final String TAG = "AuthenticationInterceptor";
    public AuthenticationInterceptor() {}

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Log.w(TAG, "intercept: " + chain.call().request().url());
        Request.Builder builder = original.newBuilder();
        if (KhumuApplication.getToken() != null) {
            builder.header("Authorization", "Bearer " + KhumuApplication.getToken());
        }

        Request request = builder.build();
        return chain.proceed(request);
    }
}


