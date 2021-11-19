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

package com.khumu.android.articleDetail;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.khumu.android.R;

public class ArticleDetailActivity extends AppCompatActivity {
    private static final String TAG = "ArticleDetailActivity";
    private Toolbar toolbar;
    private TextView toolbarBeforeTitleTV;
    private ImageView backBtnIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        toolbar = findViewById(R.id.toolbar);
        backBtnIcon = findViewById(R.id.back_ic);
        backBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleDetailActivity.super.onBackPressed();
            }
        });
        toolbarBeforeTitleTV = findViewById(R.id.toolbar_before_title);
        toolbarBeforeTitleTV.setTextColor(getColor(R.color.gray_300));
        String beforeTitle = getIntent().getStringExtra("toolbarBeforeTitle");
        if (beforeTitle == null) {
            beforeTitle = "뒤로";
        }
        toolbarBeforeTitleTV.setText(beforeTitle);
    }
}