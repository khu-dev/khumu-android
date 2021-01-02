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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khumu.android.KhumuApplication;
import com.khumu.android.MainActivity;
import com.khumu.android.R;
import com.khumu.android.data.KhumuJWT;
import com.khumu.android.repository.TokenRepository;
import com.khumu.android.signUp.SignUpActivity;
import com.khumu.android.util.Util;

import org.json.JSONException;

import java.io.IOException;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity {

    @Inject
    TokenRepository tokenRepository;
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
                            KhumuJWT jwt = tokenRepository.GetToken(username, password);
                            KhumuApplication.setKhumuConfig(jwt.getUsername(), jwt.getNickname(), jwt.toString());
                            KhumuApplication.loadKhumuConfig();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            LoginActivity.this.startActivity(intent);
                            finish();
                        } catch (TokenRepository.WrongCredentialException e) {
                            System.out.println("잘못된 계정 정보입니다.\n올바른 정보를 입력해주세요.");
                            // 기타 스레드에서는 Toast를 할 수 없다.
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "잘못된 계정 정보입니다.\n올바른 정보를 입력해주세요.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }  catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
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