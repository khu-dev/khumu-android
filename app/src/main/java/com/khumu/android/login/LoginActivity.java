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

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.khumu.android.KhumuApplication;
import com.khumu.android.MainActivity;
import com.khumu.android.R;
import com.khumu.android.data.KhumuJWT;
import com.khumu.android.data.rest.JWTErrorResponse;
import com.khumu.android.data.rest.JWTRequest;
import com.khumu.android.data.rest.JWTResponse;
import com.khumu.android.data.rest.UserResponse;
import com.khumu.android.repository.TokenService;
import com.khumu.android.signUp.Info21SignUpActivity;
import com.khumu.android.signUp.SignUpLandingActivity;
import com.khumu.android.util.FcmManager;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    final static String TAG = "LoginActivity";

    @Inject
    TokenService tokenService;
    @Inject
    FcmManager fcmManager;
    Button loginBTN;
    TextView signUpTV;
    EditText usernameET;
    EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KhumuApplication.applicationComponent.inject(this);
        setContentView(R.layout.activity_login);
        loginBTN = findViewById(R.id.login_btn);
        signUpTV = findViewById(R.id.sign_up_tv);
        usernameET = findViewById(R.id.username_et);
        passwordET = findViewById(R.id.password_et);

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("로그인 중입니다.");
                progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
                progressDialog.show();
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
                                    progressDialog.dismiss();
                                    if (response.isSuccessful()) {
                                        Log.d(TAG, "onResponse: " + response.raw());
                                        Log.d(TAG, "onResponse: " + response.body().getAccess());
                                        progressDialog.dismiss();
                                        KhumuJWT jwt = new KhumuJWT(response.body().getAccess());
                                        KhumuApplication.setKhumuConfig(jwt.getUsername(), jwt.getNickname(), jwt.toString(), KhumuApplication.getPushToken());
                                        KhumuApplication.loadKhumuConfig();
                                        fcmManager.createOrUpdatePushSubscription();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        LoginActivity.this.startActivity(intent);
                                        finish();
                                    } else {
                                        // 잘못된 credential로 인한 권한 없음.
                                        if (response.code() == 401) {
                                            // wrong value 에러 메시지 출력
                                            // di container 속 retrofit instance를 load
                                            Converter<ResponseBody, JWTErrorResponse> errorConverter =
                                                    KhumuApplication.applicationComponent.getRetrofit().responseBodyConverter(JWTErrorResponse.class, new Annotation[0]);
                                            try {
                                                JWTErrorResponse errorBody = errorConverter.convert(response.errorBody());
                                                Toast.makeText(LoginActivity.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                                            } catch (IOException e) {
                                                // 에러 바디 없는 경우.
                                                e.printStackTrace();
                                            }
                                        } else{
                                            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                                            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            LoginActivity.this.startActivity(intent);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<JWTResponse> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Log.d(TAG, "onFailure: ", t);
                                    Toast.makeText(getApplicationContext(), "서버와 통신할 수 없습니다.", Toast.LENGTH_SHORT).show();
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

        signUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(LoginActivity.this, Info21SignUpActivity.class);
                LoginActivity.this.startActivity(signUpIntent);
            }
        });

        // 자동으로 되긴하네
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