package com.khumu.android.repository;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.android.data.KhumuUser;
import com.khumu.android.util.Util;

import org.json.JSONException;

import java.io.IOException;

import javax.inject.Inject;

import dagger.Module;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Module
public class KhumuUserRepository {
    private final static String TAG="KhumuUserRepository";
    @Inject
    public KhumuUserRepository(){}

    // board는 null | board_name. null인 경우에는 그냥 board 구분 없이, board_name을 입력하면 그 board의 게시물만.
    // page는 아직 pagination을 이용한 기능이 많지 않으므로 그냥 1 page 처리
    public boolean CreateKhumuUser(KhumuUser user) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();
        String userStr = mapper.writeValueAsString(user);
        Log.d(TAG, "CreateKhumuUser: " + userStr);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), userStr);

        HttpUrl.Builder urlBuilder = Util.newBuilder()
                .addPathSegment("users");

        Request req = new Request.Builder()
                .post(body)
                .url(urlBuilder.build())
                .build();

        Response resp = client.newCall(req).execute();

        if(resp.code() == 201){
            return true;
        } else{
            return false;
        }

    }
}


