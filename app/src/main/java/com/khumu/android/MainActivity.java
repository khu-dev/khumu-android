//package com.khumu.android;
//
//import android.os.Bundle;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.navigation.ui.NavigationUI;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        Helper.Init();
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
////        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
////                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
////                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
////        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
//    }
//
//}

package com.khumu.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khumu.android.login.LoginActivity;
import com.khumu.android.signUp.SignUpActivity;
import com.khumu.android.util.Util;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    MaterialToolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Util.init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
////        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
////                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
////                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        toolbar = findViewById(R.id.toolbar);
        setToolbarInfo();
//        toolbar.findViewById()
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // login 후 돌아온 걸 수도 있음.
        setToolbarInfo();
    }

    private void setToolbarInfo(){
        ViewGroup toolbarInfo = (ViewGroup) toolbar.findViewById(R.id.layout_toolbar_info);
        ImageView userIcon = toolbar.findViewById(R.id.layout_toolbar_user_icon);
        TextView usernameTV = toolbarInfo.findViewById(R.id.layout_toolbar_nickname_tv);

        Log.d(TAG, "setToolbarInfo: " + KhumuApplication.isAuthenticated());
        Log.d(TAG, "setToolbarInfo: " + KhumuApplication.getToken());
        Log.d(TAG, "setToolbarInfo: " + KhumuApplication.getUsername());
        Log.d(TAG, "setToolbarInfo: " + KhumuApplication.getNickname());
        // unauthenticated
        if(!KhumuApplication.isAuthenticated()){
            userIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(loginIntent);
                }
            });
            usernameTV.setText("로그인해주시기 바랍니다.");
        } else{
            // authenticated => logout
            usernameTV.setText(KhumuApplication.username);

            userIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), KhumuApplication.username + " logout", Toast.LENGTH_SHORT).show();
                    KhumuApplication.clearKhumuConfig();
                    KhumuApplication.loadKhumuConfig();
                    //재귀적으로 툴바를 그린다.
                    setToolbarInfo();
                }
            });

        }

    }

}