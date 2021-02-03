package com.khumu.android.util;

import com.khumu.android.KhumuApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {

    public AuthenticationInterceptor() {}

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                .header("Authorization", "Bearer " + KhumuApplication.getToken());

        Request request = builder.build();
        return chain.proceed(request);
    }
}


