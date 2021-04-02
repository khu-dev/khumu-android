package com.khumu.android.signUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.khumu.android.R;
import com.khumu.android.repository.KhumuUserRepository;

public class SignUpLandingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_landing);
        ViewGroup info21LoginLayout = findViewById(R.id.info21_login_layout);
        info21LoginLayout.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpLandingActivity.this, Info21SignUpActivity.class);
            startActivity(intent);
        });
    }
}
