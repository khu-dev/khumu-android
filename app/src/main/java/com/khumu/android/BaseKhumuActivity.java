package com.khumu.android;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.khumu.android.login.LoginActivity;

public class BaseKhumuActivity extends AppCompatActivity {
    private final static String TAG = "BaseKhumuActivity";
    public MaterialToolbar toolbar; // toolbar를 사용할 것이라면 자손이 toolbar를 설정해주어야한다.

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // login 후 돌아온 걸 수도 있음.
        setToolbarInfo();
    }

    protected void setToolbarInfo(){
        ViewGroup toolbarInfo = (ViewGroup) toolbar.findViewById(R.id.layout_toolbar_info);
        ImageView userIcon = toolbar.findViewById(R.id.layout_toolbar_user_icon);
        TextView usernameTV = toolbarInfo.findViewById(R.id.layout_toolbar_nickname_tv);

        // unauthenticated
        if(!KhumuApplication.isAuthenticated()){
            userIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    BaseKhumuActivity.this.startActivity(loginIntent);
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
