package com.khumu.android;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.khumu.android.login.LoginActivity;

public class BaseKhumuActivity extends AppCompatActivity {
    private final static String TAG = "BaseKhumuActivity";
    public MaterialToolbar toolbar; // toolbar를 사용할 것이라면 자손이 toolbar를 설정해주어야한다.

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}
