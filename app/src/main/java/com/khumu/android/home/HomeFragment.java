package com.khumu.android.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.data.Article;
import com.khumu.android.notifications.NotificationActivity;
import com.khumu.android.qrCode.QrCodeActivity;
import com.khumu.android.repository.BoardRepository;
import com.khumu.android.repository.NotificationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class HomeFragment extends Fragment {
    WebView webView;
    Map<String, String> webViewHeaders = new HashMap<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Layout inflate 이전
        // savedInstanceState을 이용해 다룰 데이터가 있으면 다룸.
        super.onCreate(savedInstanceState);
        KhumuApplication.applicationComponent.inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // 나의 부모인 컨테이너에서 내가 그리고자 하는 녀석을 얻어옴. 사실상 루트로 사용할 애를 객체와.
        // inflate란 xml => java 객체
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        // status bar 색상
        // refs: https://stackoverflow.com/a/56433156/9471220
        this.getActivity().getWindow().setStatusBarColor(this.getActivity().getColor(R.color.red_500));
        this.getActivity().getWindow().getDecorView().setSystemUiVisibility(
                this.getActivity().getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark\
        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView = view.findViewById(R.id.home_web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webViewHeaders.put("Authorization", "Bearer " + KhumuApplication.getToken());
        webView.loadUrl("https://khumu-frontend.vercel.app/", webViewHeaders);
        // client가 없으면 그냥 기본 브라우저 사용.
        webView.setWebViewClient(new WebViewClient());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.getActivity().getWindow().setStatusBarColor(this.getActivity().getColor(R.color.white));
        this.getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
    }
}