package com.khumu.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentController;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khumu.android.home.HomeFragment;
import com.khumu.android.home.JavaScriptInterfaceImpl;
import com.khumu.android.login.LoginActivity;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    // nav_host_fragment를 이용해 current fragment를 참조하기 위함
    Fragment fragment;
    public UrlInterceptor urlInterceptor = new UrlInterceptor(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(KhumuApplication.isAuthenticated()){
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
            fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (destination.getLabel().equals(getString(R.string.title_home))) {
                    getWindow().setStatusBarColor(getColor(R.color.red_500));
                    getWindow().getDecorView().setSystemUiVisibility(
                            getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark\
                } else{
                    getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
                }
            });

            String link = getIntent().getStringExtra("link");
            if (link != null) {
                boolean result = urlInterceptor.openUrl(Uri.parse(link));
            }

        } else{
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    // onKeyDown은 안 먹히더라.
    // 아무래도 fragment가 focus를 갖고 있기 때문일까?
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
        {
            // nav_host_fragment 에서 current fragment를 구하기 위함
            // => webview를 이용하는 fragment의 경우 이전 키를 누를 수 있으면 fragment 단에서 이전 키를 처리하려고.
            // https://stackoverflow.com/questions/50689206/how-i-can-retrieve-current-fragment-in-navhostfragment
            Fragment currentFragment = fragment.getChildFragmentManager().getFragments().get(0);
            // HomeFragment 를 이용 중일 떄
            if (currentFragment instanceof HomeFragment) {
                HomeFragment homeFragment = (HomeFragment) currentFragment;
                if (homeFragment.webView.canGoBack()) {
                    System.out.println("웹뷰에서 뒤로 갈 수 있기 때문에 MainActivity에서 뒤로 가기를 수행하지 않고 웹뷰가 뒤로 가기를 수행합니다.");
                    homeFragment.webView.goBack();
                    return true;
                }
            }
        } else{
        }

        return super.dispatchKeyEvent(event);
    }
}