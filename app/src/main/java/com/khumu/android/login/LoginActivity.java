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
import com.khumu.android.R;
import com.khumu.android.repository.TokenRepository;
import com.khumu.android.signUp.SignUpActivity;
import com.khumu.android.util.Util;

import org.json.JSONException;

import java.io.IOException;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity {

    @Inject
    TokenRepository tokenRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KhumuApplication.container.inject(this);
        setContentView(R.layout.activity_login);
        Button loginBTN = findViewById(R.id.login_btn);
        Button signUpBTN = findViewById(R.id.sign_up_btn);
        EditText usernameET = findViewById(R.id.login_username_et);
        EditText passwordET = findViewById(R.id.login_password_et);

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            String username = usernameET.getText().toString();
                            String password = passwordET.getText().toString();
                            String token = tokenRepository.GetToken(username, password);

                            if(token == "" || token == null){
                                throw new TokenRepository.WrongCredentialException();
                            }

                            SharedPreferences.Editor editor = KhumuApplication.sharedPref.edit();
                            editor.putString("username", username);
                            editor.putString("nickname", "");
                            editor.putString("token", token);
                            editor.commit();
                            KhumuApplication.loadKhumuConfig();

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

    }


}