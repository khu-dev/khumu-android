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

package com.khumu.android.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.khumu.android.KhumuApplication;
import com.khumu.android.MainActivity;
import com.khumu.android.R;
import com.khumu.android.data.KhumuJWT;
import com.khumu.android.data.rest.JWTRequest;
import com.khumu.android.data.rest.JWTResponse;
import com.khumu.android.retrofitInterface.TokenService;
import com.khumu.android.signUp.SignUpActivity;
import com.khumu.android.util.FcmManager;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    final static String TAG = "LoginActivity";

    @Inject
    TokenService tokenService;
    @Inject
    FcmManager fcmManager;
    Button loginBTN;
    Button signUpBTN;
    EditText usernameET;
    EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KhumuApplication.container.inject(this);
        setContentView(R.layout.activity_login);
        loginBTN = findViewById(R.id.login_btn);
        signUpBTN = findViewById(R.id.sign_up_btn);
        usernameET = findViewById(R.id.login_username_et);
        passwordET = findViewById(R.id.login_password_et);

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            String username = usernameET.getText().toString();
                            String password = passwordET.getText().toString();
                            Call<JWTResponse> call = tokenService.postToken("application/json", new JWTRequest(username, password));
                            call.enqueue(new Callback<JWTResponse>() {
                                @Override
                                public void onResponse(Call<JWTResponse> call, Response<JWTResponse> response) {
                                    Log.d(TAG, "onResponse: " + response.raw());
                                    Log.d(TAG, "onResponse: " + response.body().getAccess());
                                    KhumuJWT jwt = new KhumuJWT(response.body().getAccess());
                                    KhumuApplication.setKhumuConfig(jwt.getUsername(), jwt.getNickname(), jwt.toString(), KhumuApplication.getPushToken());
                                    KhumuApplication.loadKhumuConfig();
                                    fcmManager.createOrUpdatePushSubscription();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    LoginActivity.this.startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<JWTResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure: ", t);
                                    Toast.makeText(getApplicationContext(), "잘못된 계정 정보입니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "로그인 관련 알 수 없는 문제 발생.\n관리자에게 문의해주세요..ㅜㅜ", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }.start();
            }
        });

        signUpBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                LoginActivity.this.startActivity(signUpIntent);
            }
        });

        passwordET.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    loginBTN.callOnClick();
                    return false;
                }
                return false;
            }
        });
    }
}