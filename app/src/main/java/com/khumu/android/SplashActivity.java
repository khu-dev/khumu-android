package com.khumu.android;//package com.khumu.android;
//
//import android.content.Intent;
//import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khumu.android.MainActivity;
import com.khumu.android.home.HomeFragment;
import com.khumu.android.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 3000L);


    }
}