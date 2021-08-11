package com.khumu.android.component;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.khumu.android.R;
import com.khumu.android.signUp.Info21SignUpActivity;

import java.time.Duration;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AutoDismissAlertDialog extends SweetAlertDialog {
    Long timeoutMillis;
    public AutoDismissAlertDialog(Context context, int alertType, String title, String content, Long timeoutMillis) {
        super(context, alertType);
        hideConfirmButton();
        setTitleText(title);
        setContentText(content);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        this.timeoutMillis = timeoutMillis;
    }

    @Override
    public void show() {
        super.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                 AutoDismissAlertDialog.this.dismiss();
            }
        }, timeoutMillis);
    }

//    @Override
//    public void onClick(View view) {
//        super.onClick();
//    }
}
