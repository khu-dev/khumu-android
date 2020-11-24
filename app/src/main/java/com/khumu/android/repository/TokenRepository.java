package com.khumu.android.repository;

import android.util.Log;

import com.khumu.android.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import dagger.Module;
import okhttp3.*;
@Module
public class TokenRepository {
    public final static String TAG = "TokenRepository";
    @Inject
    public TokenRepository(){}
    public String GetToken(String username, String password) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        RequestBody authBody = RequestBody.create(MediaType.parse("application/json"),
                String.format("{\"username\":\"%s\",\"password\":\"%s\"}", Util.DEFAULT_USERNAME, Util.DEFAULT_PASSWORD)
        );

        HttpUrl.Builder urlBuilder = Util.newBuilder()
            .addPathSegment("token");

        Request authReq = new Request.Builder()
            .post(authBody)
            .url(urlBuilder.build())
            .build();
        Response authResp = client.newCall(authReq).execute();
        String authRespStr = authResp.body().string();
        String token = new JSONObject(authRespStr).getString("access");
        return token;
    }
}
