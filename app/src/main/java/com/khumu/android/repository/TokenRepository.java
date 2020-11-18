package com.khumu.android.repository;

import com.khumu.android.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.*;

public class TokenRepository {
    public TokenRepository(){}
    public String GetToken(String username, String password) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        RequestBody authBody = RequestBody.create(MediaType.parse("application/json"),
            String.format("{\"username\":\"%s\",\"password\":\"%s\"}", Util.DEFAULT_USERNAME, Util.DEFAULT_PASSWORD)
        );
        Request authReq = new Request.Builder()
                .post(authBody)
                .url(Util.APIRootEndpoint + "token")
                .build();
        Response authResp = client.newCall(authReq).execute();
        String authRespStr = authResp.body().string();
        String token = new JSONObject(authRespStr).getString("access");
        return token;
    }
}
