package com.khumu.android.image;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.load.model.ResourceLoader;
import com.khumu.android.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lombok.val;

public class ImageDetailActivity extends AppCompatActivity {
    private final static String TAG = "ImageDetailActivity";
    ViewPager2 viewPager;
    List<String> imageFileNames = new ArrayList<>();
    TextView titleTV;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        titleTV = findViewById(R.id.download_title_tv);
        viewPager = findViewById(R.id.image_detail_view_pager);
        imageFileNames.addAll((List<String>) getIntent().getSerializableExtra("imageFileNames"));
        Integer currentImageIndex = getIntent().getIntExtra("currentImageIndex", 0);
        if (imageFileNames == null) {
            imageFileNames = new ArrayList<>();
        }
        titleTV.setText((currentImageIndex + 1) +" / " + imageFileNames.size());

        viewPager.setAdapter(new DetailImageAdapter(this, imageFileNames));
        viewPager.setCurrentItem(currentImageIndex, false);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                titleTV.setText((position + 1) + " / " + imageFileNames.size());
                super.onPageSelected(position);
            }
        });
    }

    public void onClickCloseBTN(View view) {
        this.finish();
    }

    // 다운로드 구현 참고: https://khs613.github.io/android/download-manager/
    public void onClickDownloadBTN(View view) {
        int currentImageIdx = viewPager.getCurrentItem();
        String outputFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "dev_submit.mp4";
        DownloadManager downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        File outputFile = new File(outputFilePath);
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
        Uri downloadUri = Uri.parse("https://storage.khumu.jinsu.me" + "/original/" + imageFileNames.get(currentImageIdx));

        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        List<String> pathSegmentList = downloadUri.getPathSegments();
        request.setTitle("다운로드 항목");
        request.setDestinationUri(Uri.fromFile(outputFile));
        request.setAllowedOverMetered(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        Log.d(TAG, "onClickDownloadBTN: 다운로드 작업 enqueue (url=" + "https://storage.khumu.jinsu.me" + "/original/" + imageFileNames.get(currentImageIdx) + ")");
        downloadManager.enqueue(request);

    }
}
