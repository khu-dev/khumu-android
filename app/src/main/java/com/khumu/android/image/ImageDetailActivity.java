package com.khumu.android.image;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.khumu.android.R;

import java.util.ArrayList;
import java.util.List;

public class ImageDetailActivity extends AppCompatActivity {
    ViewPager2 viewPager;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        viewPager = findViewById(R.id.image_detail_view_pager);
        List<String> imageFileNames = (List<String>) getIntent().getSerializableExtra("imageFileNames");
        Integer currentImageIndex = getIntent().getIntExtra("currentImageIndex", 0);
        if (imageFileNames == null) {
            imageFileNames = new ArrayList<>();
        }

        viewPager.setAdapter(new DetailImageAdapter(this, imageFileNames));
        viewPager.setCurrentItem(currentImageIndex);
    }
}
