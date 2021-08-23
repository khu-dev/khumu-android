package com.khumu.android;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.khumu.android.JavaScriptInterfaceImpl;
import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.UrlInterceptor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class BaseWebViewFragment extends Fragment {
    private final static String TAG = "BaseWebViewFragment";
    private UrlInterceptor urlInterceptor;
    public WebView webView;

    Map<String, String> webViewHeaders = new HashMap<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Layout inflate 이전
        // savedInstanceState을 이용해 다룰 데이터가 있으면 다룸.
        super.onCreate(savedInstanceState);
        urlInterceptor = new UrlInterceptor(this.getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // 나의 부모인 컨테이너에서 내가 그리고자 하는 녀석을 얻어옴. 사실상 루트로 사용할 애를 객체와.
        // inflate란 xml => java 객체
        View root = inflater.inflate(getFragmentLayout(), container, false);
        // status bar 색상
        // refs: https://stackoverflow.com/a/56433156/9471220

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView = view.findViewById(R.id.web_view);
        /**
         * 웹에서는 아래와 같은 코드로 이용 가능
         * <script type="text/javascript">
         *     function setTokenByAndroid(toast) {
         *         token = Android.getToken(toast);
         *     }
         * </script>
         */
        webViewHeaders.put("Authorization", "Bearer " + KhumuApplication.getToken());
        System.out.println("open  webview");

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
//        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            // return true => webview가 해당 url load을 하지않음. 우리가 처리한 걸로 끝.
            // return false => webview가 결국 해당 url을 load
            // ref: https://stackoverflow.com/questions/39979950/webviewclient-not-calling-shouldoverrideurlloading
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                boolean result = urlInterceptor.openUrl(uri);
                return result;
            }
        });
        // webview chrome client도 있어야 alert를 이용할 수 있더라
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new JavaScriptInterfaceImpl(this.getContext(), KhumuApplication.getToken()), "Android");
        webView.loadUrl(getLoadUrl());
//        webView.loadUrl("javascript:alert(Android.getToken())");
    }

    protected abstract int getFragmentLayout();
    protected abstract String getLoadUrl();
}