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

package com.khumu.android.signUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.khumu.android.R;
import com.khumu.android.data.KhumuUser;
import com.khumu.android.repository.KhumuUserRepository;

import org.json.JSONException;

import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {
    public KhumuUserRepository khumuUserRepository;
    private EditText usernameET;
    private EditText passwordET;
    private EditText passwordConfirmET;
    private EditText nicknameET;
    private EditText emailET;
    private EditText studentNumberET;
    private EditText departmentNumber;
    private Button signUpBTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.khumuUserRepository = new KhumuUserRepository();

        findViews();
        addEventListeners();

        setTestInput();
    }

    private void findViews(){
        this.usernameET = findViewById(R.id.sign_up_username_et);
        this.passwordET = findViewById(R.id.sign_up_password_et);
        this.passwordConfirmET = findViewById(R.id.sign_up_password_confirm_et);
        this.nicknameET = findViewById(R.id.sign_up_nickname_et);
        this.emailET = findViewById(R.id.sign_up_email_et);
        this.studentNumberET = findViewById(R.id.sign_up_student_number_et);
        this.departmentNumber = findViewById(R.id.sign_up_department_et);
        this.signUpBTN = findViewById(R.id.sign_up_btn);
    }

    private void addEventListeners(){
        this.signUpBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KhumuUser user = new KhumuUser(
                    usernameET.getText().toString(),
                    nicknameET.getText().toString(),
                    passwordET.getText().toString(),
                    emailET.getText().toString(),
                    studentNumberET.getText().toString(),
                    departmentNumber.getText().toString()
                );

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            boolean isUserCreated = SignUpActivity.this.khumuUserRepository.CreateKhumuUser(user);
                            if (isUserCreated) {
                                String createdUsername = usernameET.getText().toString();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), createdUsername + "님 Khumu 가입을 환영합니다 >_<\n", Toast.LENGTH_LONG).show();
                                    }
                                });
                                SignUpActivity.this.finish();
                            } else {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "계정을 생성하지 못했습니다. ㅜㅜ", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }.start();
            }
        });
    }

    // test용으로 input값을 집어넣을 때에만 사용
    private void setTestInput(){
        this.usernameET.setText(""+java.util.Calendar.getInstance().getTimeInMillis());
        this.passwordET.setText("123123");
        this.passwordConfirmET.setText("123123");
        this.nicknameET.setText("" + java.util.Calendar.getInstance().getTimeInMillis());
        this.emailET.setText("testJinsu@khu.ac.kr");
        this.studentNumberET.setText("2000123123");
        this.departmentNumber.setText("컴퓨터공학과");
    }
}