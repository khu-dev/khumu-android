package com.khumu.android;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.khumu.android.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JavaScriptInterfaceImpl {
    String token;
    Context context;
    public JavaScriptInterfaceImpl(Context context, String token) {
        this.context = context;
        this.token = token;
    }

    @JavascriptInterface
    public String getToken() {
        return this.token;
    }

    @JavascriptInterface
    public void showToast(String s) {
        Toast.makeText(this.context, s, Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public String getVersionInfo() {
//        int versionCode = BuildConfig.VERSION_CODE;
        String currentVersion = BuildConfig.VERSION_NAME;
        String latestVersion = "최신 버전 정보를 가져올 수 없습니다.";
        // https://stackoverflow.com/a/19028827/9471220
        OkHttpClient http = new OkHttpClient();
        Call call = http.newCall(new Request.Builder().
                url("https://api.github.com/repos/khu-dev/khumu-android/releases")
                .get()
                .build());
        try {
            Response response = call.execute();
            JSONArray releases = new JSONArray(response.body().string());
            for (int i = 0; i < releases.length(); i++) {
                JSONObject release = releases.getJSONObject(i);
                if (release.getBoolean("draft") == false && release.getBoolean("prerelease") == false) {
                    latestVersion = release.getString("tag_name");
                    break;
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return new Gson().toJson(new VersionInfo(currentVersion, latestVersion));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class VersionInfo{
        @SerializedName("current_version")
        String currentVersion;
        @SerializedName("latest_version")
        String latestVersion;
    }
}
