package com.khumu.android.repository;

import com.auth0.android.jwt.JWT;
import com.khumu.android.data.KhumuJWT;
import com.khumu.android.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import dagger.Module;
import okhttp3.*;
public class TokenRepository {
    public final static String TAG = "TokenRepository";
    @Inject
    public TokenRepository(){}

    public static class WrongCredentialException extends Exception{}

    public KhumuJWT GetToken(String username, String password) throws IOException, JSONException, WrongCredentialException {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("username",username)
                .add("password", password);

        RequestBody formBody = formBuilder.build();

        HttpUrl.Builder urlBuilder = Util.newBuilder()
            .addPathSegment("token");

        Request authReq = new Request.Builder()
            .post(formBody)
            .url(urlBuilder.build())
            .build();
        Response authResp = client.newCall(authReq).execute();

        if(authResp.code() == 401){
            throw new WrongCredentialException();
        }

        String authRespStr = authResp.body().string();
        String tokenStr = new JSONObject(authRespStr).getString("access");
        return new KhumuJWT(tokenStr);
    }
}
