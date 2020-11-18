//package com.khumu.android.util;
//
//import org.json.JSONObject;
//
//import okhttp3.Callback;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import okhttp3.OkHttpClient;
//import okhttp3.*;
//import android.os.AsyncTask;
//
//import java.util.HashMap;
//
//public class Http {
//
//    private OkHttpClient client;
//    public Http() {
//        client = new OkHttpClient();
//    }
//    /** 웹 서버로 요청을 한다. */
//    public void Get(String url, HashMap<String, String>zheader Callback callback) {
//        RequestBody body = new FormBody.Builder()
//                .add("parameter", parameter)
//                .add("parameter2", parameter2)
//                .build();
//        Request request = new Request.Builder()
//                .url("http://mydomain.com/sendData")
//                .post(body)
//                .build();
//        client.newCall(request).enqueue(callback);
//    }
//
//
//    출처: https://thereclub.tistory.com/46 [강남부자]
////
////    RequestBody authBody = RequestBody.create(MediaType.parse("application/json"),
////            String.format("{\"username\":\"%s\",\"password\":\"%s\"}", Util.DEFAULT_USERNAME, Util.DEFAULT_PASSWORD)
////    );
////    Request authReq = new Request.Builder()
////            .post(authBody)
////            .url(Util.APIRootEndpoint + "token")
////            .build();
////    Response authResp = client.newCall(authReq).execute();
////    String authRespStr = authResp.body().string();
////    String token = new JSONObject(authRespStr).getString("access");