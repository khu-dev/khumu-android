package com.khumu.android.home;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class JavaScriptInterface {
    String token;
    Context context;
    public JavaScriptInterface(Context context, String token) {

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
}
